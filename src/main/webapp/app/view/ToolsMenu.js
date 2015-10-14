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
Ext.define('Ssp.view.ToolsMenu', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.toolsmenu',
    mixins: ['Deft.mixin.Injectable',
        'Deft.mixin.Controllable'
    ],
    controller: 'Ssp.controller.ToolsViewController',
    inject: {
        appEventsController: 'appEventsController',
        columnRendererUtils: 'columnRendererUtils',
        store: 'toolsStore',
        textStore: 'sspTextStore'
    },
    initComponent: function() {
        var me = this;
        Ext.apply(this, {
            width: '100%',
            height: '100%',
            store: this.store,

            features: [{
                id: 'group',
                ftype: 'grouping',
                groupHeaderTpl: '{name}',
                hideGroupedHeader: false,
                enableGroupingMenu: false
            }],

            columns: [{
                header: me.textStore.getValueByCode('ssp.label.tools', 'Tools'),
                dataIndex: "name",
                sortable: false,
                menuDisabled: true,
                flex: 1
            }, {
                xtype: 'actioncolumn',
                width: 18,
                header: '',
                sortable: false,
                hidden: true,
                items: [{
                    tooltip: 'Add Tool',
                    // icon: Ssp.util.Constants.ADD_TOOL_ICON_PATH,
                    getClass: this.columnRendererUtils.renderAddToolIcon,
                    handler: function(grid, rowIndex, colIndex) {
                        var rec = grid.getStore().getAt(rowIndex);
                        var panel = grid.up('panel');
                        //panel.toolId.data=rec.data.toolId;
                        panel.appEventsController.getApplication().fireEvent('addTool');
                        Ext.Msg.alert('Attention', 'This feature is not yet active');
                    },
                    scope: this
                }]
            }]
        });

        return this.callParent(arguments);
    }
});