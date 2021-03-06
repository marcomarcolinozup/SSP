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
Ext.define('Ssp.view.person.Appointment', {
    extend: 'Ext.form.Panel',
    alias: 'widget.personappointment',
    mixins: ['Deft.mixin.Injectable', 'Deft.mixin.Controllable'],
    controller: 'Ssp.controller.person.AppointmentViewController',
    inject: {
        studentTypesStore: 'studentTypesAllUnpagedStore',
        textStore: 'sspTextStore'
    },
	width: '100%',
	padding: '0 0 0 0',
	layout: {
		type:'vbox'
	},
    initComponent: function(){
        var me = this;
        var defaultSyncdLabel = '<span class="syncedField">(sync\'d)</span>  ';
        Ext.apply(me, {
            fieldDefaults: {
                msgTarget: 'side',
                labelAlign: 'top',
                labelWidth: 200
            },
            border: 0,
            padding: 0,
            items: [{
                xtype: 'container',
                border: 0,
                padding: 0,
                title: '',
                defaultType: 'textfield',
                /*defaults: {
                    anchor: '50%'
                },*/
                items: [{
                    xtype: 'combobox',
                    name: 'studentTypeId',
                    itemId: 'studentTypeCombo',
                    id: 'studentTypeCombo',
                    fieldLabel: me.textStore.getValueByCode('ssp.label.syncd',defaultSyncdLabel) + me.textStore.getValueByCode('ssp.label.student-type','Student Type'),
                    emptyText: me.textStore.getValueByCode('ssp.empty-text.appointment.student-type','Select One'),
                    store: me.studentTypesStore,
                    valueField: 'id',
                    displayField: 'name',
                    mode: 'local',
                    typeAhead: false,
                    editable: false,
                    queryMode: 'local',
                    allowBlank: false,
					width: 250
                }, {
                    xtype: 'datefield',
                    fieldLabel:  me.textStore.getValueByCode('ssp.label.appointment.date','Appointment Date'),
                    itemId: 'appointmentDateField',
                    altFormats: 'm/d/Y|m-d-Y',
                    invalidText: me.textStore.getValueByCode('ssp.invalid-text.appointment.date','{0} is not a valid date - it must be in the format: 06/21/2012 or 06-21-2012'),
                    name: 'appointmentDate',
                    allowBlank: me.instantCaseloadAssignment == true ? true:false,
                    showToday: false,
                    hidden: me.instantCaseloadAssignment == true ? true:false
                }, {
                    xtype: 'container',
                    title: '',
                    layout: {
                        align: 'stretch',
                        type: 'hbox'
                    },
                    hidden: me.instantCaseloadAssignment == true ? true:false,
                    border: 0,
                    padding: '0 0 0 0',
                    items: [{
                        xtype: 'timefield',
                        name: 'startTime',
                        itemId: 'startTimeField',
                        fieldLabel:  me.textStore.getValueByCode('ssp.label.appointment.start-time','Appointment Start'),
                        labelSeparator: "",
                        increment: 30,
                        typeAhead: false,
                        allowBlank: me.instantCaseloadAssignment == true ? true:false,
                        width: 110,
                        margin: '0 0 0 0',
                        padding: '0 0 0 0',
                        listeners: {
                            'select': function(){
                                Ext.ComponentQuery.query('#endTimeField')[0].setMinValue(this.getValue());
                            }
                        }
                    }, {
                        xtype: 'timefield',
                        name: 'endTime',
                        itemId: 'endTimeField',
                        fieldLabel: me.textStore.getValueByCode('ssp.label.appointment.end-time','- End Times'),
                        typeAhead: false,
                        allowBlank: me.instantCaseloadAssignment == true ? true:false,
                        increment: 30,
                        width: 110,
                        margin: '0 0 0 0',
                        padding: '0 0 0 5',
                        listeners: {
                            'select': function(){
                                if (Ext.ComponentQuery.query('#startTimeField')[0].getValue() !== null) {
                                    if (this.getValue() < Ext.ComponentQuery.query('#startTimeField')[0].getValue()) {
                                        alert('Error! End Date Must Be Later Than The Start Date.')
                                        this.setValue(Ext.ComponentQuery.query('#startTimeField')[0].getValue())
                                    };
                             };
                                
                          }
                        }
                    }]
                }]
            }]
        });
        
        return me.callParent(arguments);
    }
});
