/*
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
Ext.define('Ssp.view.person.CaseloadAssignment', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.caseloadassignment',
    mixins: ['Deft.mixin.Injectable', 'Deft.mixin.Controllable'],
    controller: 'Ssp.controller.person.CaseloadAssignmentViewController',
    inject: {
        model: 'currentPerson',
        textStore: 'sspTextStore'
    },
    width: '100%',
    height: '100%',
	
    initComponent: function(){
        var me = this;
        Ext.apply(me, {
            title: " ",
            autoScroll: true,
            defaults: {
                bodyStyle: 'padding:5px'
            },
			layout:{
				type: 'fit'
			},
			items:[{
				xtype : 'tabpanel',
				
				itemId: 'caseloadPanel',
				deferredRender: false
			}],
            dockedItems: [{
                xtype: 'toolbar',
                dock: 'top',
                items: [{
                    xtype: 'button',
                    itemId: 'saveButton',
                    text: me.textStore.getValueByCode('ssp.label.save-button','Save')
                    
                }, '-', {
                    xtype: 'button',
                    itemId: 'cancelButton',
                    text: me.textStore.getValueByCode('ssp.label.cancel-button','Cancel')
                }]
            
            },  {
                dock: 'bottom',
                xtype: 'toolbar',
                items: [                
                 {
                    xtype: 'tbspacer',
                    flex: 1
                }, {
                    xtype: 'button',
                    itemId: 'printButton',
                    tooltip: me.textStore.getValueByCode('ssp.tooltip.caseload-assignment.print-button','Print Appointment Form'),
                    hidden: true,
                    width: 30,
                    height: 30,
                    cls: 'printIcon'
                }, {
                    xtype: 'button',
                    itemId: 'emailButton',
                    hidden: true,
                    tooltip: me.textStore.getValueByCode('ssp.tooltip.caseload-assignment.email-button','Email Appointment Form'),
                    width: 30,
                    height: 30,
                    cls: 'emailIcon'
                }]
            }]
        });
        
        return me.callParent(arguments);
    }
    
});