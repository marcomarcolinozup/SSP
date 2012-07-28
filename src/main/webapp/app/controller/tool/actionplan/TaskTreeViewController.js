Ext.define('Ssp.controller.tool.actionplan.TaskTreeViewController', {
    extend: 'Deft.mvc.ViewController',
    mixins: [ 'Deft.mixin.Injectable' ],
    inject: {
    	apiProperties: 'apiProperties',
        appEventsController: 'appEventsController',
        person: 'currentPerson',
        task: 'currentTask',
    	treeUtils: 'treeRendererUtils'
    },
    config: {
    	categoryUrl: '',
    	challengeUrl: '',
    	challengeReferralUrl: '',
    	personChallengeUrl: ''
    },
    control: {
    	view: {
    		itemexpand: 'onItemExpand',
    		itemClick: 'onItemClick',
    		viewready: 'onViewReady'
    	},
   	
    	/*
    	'searchButton': {
			click: 'onSearchClick'
		}
		*/  	
    },
    
	onViewReady: function() {
		var rootNode = null;
		
		this.categoryUrl = this.apiProperties.getItemUrl('category');
		this.challengeUrl = this.apiProperties.getItemUrl('challenge');
		this.challengeReferralUrl = this.apiProperties.getItemUrl('challengeReferral');
		this.personChallengeUrl = this.apiProperties.getItemUrl('personChallenge');
		this.personChallengeUrl = this.personChallengeUrl.replace('{id}',this.person.get('id'));

		// clear the categories
		this.treeUtils.clearRootCategories();

    	// load student intake challenges
     	this.treeUtils.appendChildren(null,[{
	        text: 'Student Intake Challenges',
	        id: '0'+'_studentIntakeChallenge',
	        leaf: false,
	        destroyBeforeAppend: false
	      }]);
   
    	// load "all" challenges category
     	this.treeUtils.appendChildren(null,[{
	        text: 'All',
	        id: '0'+'_all',
	        leaf: false,
	        destroyBeforeAppend: false
	      }]);     	
     	
    	// load the categories
    	var treeRequest = new Ssp.model.util.TreeRequest();
    	treeRequest.set('url', this.categoryUrl);
    	treeRequest.set('nodeType','category');
    	treeRequest.set('isLeaf', false);
    	treeRequest.set('enableCheckedItems', false);	
    	this.treeUtils.getItems( treeRequest );
    },
    
    onItemExpand: function(nodeInt, obj){
    	var me=this;
    	var node = nodeInt;
    	var url = "";
    	var nodeType = "";
    	var isLeaf = false;
    	var nodeName =  me.treeUtils.getNameFromNodeId( node.data.id );
    	var id = me.treeUtils.getIdFromNodeId( node.data.id );
 
    	switch ( nodeName )
    	{
    		case 'category':
    			url = me.categoryUrl + '/' + id + '/challenge/';
    			nodeType = 'challenge';
    			break;
    			
    		case 'studentIntakeChallenge':
    			url = me.personChallengeUrl;
    			nodeType = 'challenge';
     			break;

    		case 'all':
    			url = me.challengeUrl;
    			nodeType = 'challenge';
     			break;     			
     			
    		case 'challenge':
    			url = me.challengeUrl + '/' + id + '/challengeReferral/';
    			nodeType = 'referral';
    			isLeaf = true;
    			break;
    	}
    	
    	if (url != "")
    	{
        	var treeRequest = new Ssp.model.util.TreeRequest();
        	treeRequest.set('url', url);
        	treeRequest.set('nodeType', nodeType);
        	treeRequest.set('isLeaf', isLeaf);
        	treeRequest.set('nodeToAppendTo', node);
        	treeRequest.set('enableCheckedItems',false);
        	treeRequest.set('callbackFunc', me.onLoadComplete);
        	treeRequest.set('callbackScope', me);
        	me.treeUtils.getItems( treeRequest );

        	me.getView().setLoading( true );        	
    	}
    },
    
    onLoadComplete: function( scope ){
    	scope.getView().setLoading( false );
    },
    
    /*
    onSearchClick: function(){
    	console.log('TaskTreeViewController->onSearchClick');
    	Ext.Msg.alert('Attention', 'This is a beta item. Awaiting API methods to utilize for search.'); 
    },
    */
    
    onItemClick: function(view, record, item, index, e, eOpts){
    	var me=this;
    	var successFunc;
    	var name = me.treeUtils.getNameFromNodeId( record.data.id );
    	var id = me.treeUtils.getIdFromNodeId( record.data.id );
    	var challengeId = me.treeUtils.getIdFromNodeId( record.data.parentId );
    	var confidentialityLevelId = "afe3e3e6-87fa-11e1-91b2-0026b9e7ff4c";
    	if (name=='referral')
    	{
	    	successFunc = function(response,view){
		    	var r = Ext.decode(response.responseText);
		    	if (r)
		    	{
		    		me.task.set('name', r.name);
		    		me.task.set('description', r.description);
		    		me.task.set('challengeReferralId', r.id);
		    		me.task.set('challengeId', challengeId);
		    		me.task.set('confidentialityLevel', {id: confidentialityLevelId});
		    		me.appEventsController.getApplication().fireEvent('loadTask');
		    	}		
			};
	    	
	    	me.apiProperties.makeRequest({
				url: me.apiProperties.createUrl( me.challengeReferralUrl+'/'+id ),
				method: 'GET',
				jsonData: '',
				successFunc: successFunc 
			});
    	
    	}
    }
});