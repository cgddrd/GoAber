﻿//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated by a tool.
//     Runtime Version:4.0.30319.42000
//
//     Changes to this file may cause incorrect behavior and will be lost if
//     the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

namespace GoAber.WebServiceClientSandbox.Consumer.GoAberWS {
    
    
    [System.CodeDom.Compiler.GeneratedCodeAttribute("System.ServiceModel", "4.0.0.0")]
    [System.ServiceModel.ServiceContractAttribute(ConfigurationName="GoAberWS.GoAberWSSoap")]
    public interface GoAberWSSoap {
        
        [System.ServiceModel.OperationContractAttribute(Action="http://tempuri.org/HelloWorld", ReplyAction="*")]
        [System.ServiceModel.XmlSerializerFormatAttribute(SupportFaults=true)]
        string HelloWorld();
        
        [System.ServiceModel.OperationContractAttribute(Action="http://tempuri.org/HelloWorld", ReplyAction="*")]
        System.Threading.Tasks.Task<string> HelloWorldAsync();
        
        // CODEGEN: Generating message contract since message AddActivityDataRequest has headers
        [System.ServiceModel.OperationContractAttribute(Action="http://tempuri.org/AddActivityData", ReplyAction="*")]
        [System.ServiceModel.XmlSerializerFormatAttribute(SupportFaults=true)]
        GoAber.WebServiceClientSandbox.Consumer.GoAberWS.AddActivityDataResponse AddActivityData(GoAber.WebServiceClientSandbox.Consumer.GoAberWS.AddActivityDataRequest request);
        
        [System.ServiceModel.OperationContractAttribute(Action="http://tempuri.org/AddActivityData", ReplyAction="*")]
        System.Threading.Tasks.Task<GoAber.WebServiceClientSandbox.Consumer.GoAberWS.AddActivityDataResponse> AddActivityDataAsync(GoAber.WebServiceClientSandbox.Consumer.GoAberWS.AddActivityDataRequest request);
    }
    
    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("System.Xml", "4.6.81.0")]
    [System.SerializableAttribute()]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [System.Xml.Serialization.XmlTypeAttribute(Namespace="http://tempuri.org/")]
    public partial class AuthHeader : object, System.ComponentModel.INotifyPropertyChanged {
        
        private string authtokenField;
        
        private System.Xml.XmlAttribute[] anyAttrField;
        
        /// <remarks/>
        [System.Xml.Serialization.XmlElementAttribute(Order=0)]
        public string authtoken {
            get {
                return this.authtokenField;
            }
            set {
                this.authtokenField = value;
                this.RaisePropertyChanged("authtoken");
            }
        }
        
        /// <remarks/>
        [System.Xml.Serialization.XmlAnyAttributeAttribute()]
        public System.Xml.XmlAttribute[] AnyAttr {
            get {
                return this.anyAttrField;
            }
            set {
                this.anyAttrField = value;
                this.RaisePropertyChanged("AnyAttr");
            }
        }
        
        public event System.ComponentModel.PropertyChangedEventHandler PropertyChanged;
        
        protected void RaisePropertyChanged(string propertyName) {
            System.ComponentModel.PropertyChangedEventHandler propertyChanged = this.PropertyChanged;
            if ((propertyChanged != null)) {
                propertyChanged(this, new System.ComponentModel.PropertyChangedEventArgs(propertyName));
            }
        }
    }
    
    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("System.Xml", "4.6.81.0")]
    [System.SerializableAttribute()]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [System.Xml.Serialization.XmlTypeAttribute(Namespace="http://tempuri.org/")]
    public partial class ActivityData : object, System.ComponentModel.INotifyPropertyChanged {
        
        private int categoryUnitIdField;
        
        private int valueField;
        
        private System.DateTime dateField;
        
        /// <remarks/>
        [System.Xml.Serialization.XmlElementAttribute(Order=0)]
        public int categoryUnitId {
            get {
                return this.categoryUnitIdField;
            }
            set {
                this.categoryUnitIdField = value;
                this.RaisePropertyChanged("categoryUnitId");
            }
        }
        
        /// <remarks/>
        [System.Xml.Serialization.XmlElementAttribute(Order=1)]
        public int value {
            get {
                return this.valueField;
            }
            set {
                this.valueField = value;
                this.RaisePropertyChanged("value");
            }
        }
        
        /// <remarks/>
        [System.Xml.Serialization.XmlElementAttribute(Order=2)]
        public System.DateTime date {
            get {
                return this.dateField;
            }
            set {
                this.dateField = value;
                this.RaisePropertyChanged("date");
            }
        }
        
        public event System.ComponentModel.PropertyChangedEventHandler PropertyChanged;
        
        protected void RaisePropertyChanged(string propertyName) {
            System.ComponentModel.PropertyChangedEventHandler propertyChanged = this.PropertyChanged;
            if ((propertyChanged != null)) {
                propertyChanged(this, new System.ComponentModel.PropertyChangedEventArgs(propertyName));
            }
        }
    }
    
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.CodeDom.Compiler.GeneratedCodeAttribute("System.ServiceModel", "4.0.0.0")]
    [System.ComponentModel.EditorBrowsableAttribute(System.ComponentModel.EditorBrowsableState.Advanced)]
    [System.ServiceModel.MessageContractAttribute(WrapperName="AddActivityData", WrapperNamespace="http://tempuri.org/", IsWrapped=true)]
    public partial class AddActivityDataRequest {
        
        [System.ServiceModel.MessageHeaderAttribute(Namespace="http://tempuri.org/")]
        public GoAber.WebServiceClientSandbox.Consumer.GoAberWS.AuthHeader AuthHeader;
        
        [System.ServiceModel.MessageBodyMemberAttribute(Namespace="http://tempuri.org/", Order=0)]
        public GoAber.WebServiceClientSandbox.Consumer.GoAberWS.ActivityData[] data;
        
        public AddActivityDataRequest() {
        }
        
        public AddActivityDataRequest(GoAber.WebServiceClientSandbox.Consumer.GoAberWS.AuthHeader AuthHeader, GoAber.WebServiceClientSandbox.Consumer.GoAberWS.ActivityData[] data) {
            this.AuthHeader = AuthHeader;
            this.data = data;
        }
    }
    
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.CodeDom.Compiler.GeneratedCodeAttribute("System.ServiceModel", "4.0.0.0")]
    [System.ComponentModel.EditorBrowsableAttribute(System.ComponentModel.EditorBrowsableState.Advanced)]
    [System.ServiceModel.MessageContractAttribute(WrapperName="AddActivityDataResponse", WrapperNamespace="http://tempuri.org/", IsWrapped=true)]
    public partial class AddActivityDataResponse {
        
        [System.ServiceModel.MessageBodyMemberAttribute(Namespace="http://tempuri.org/", Order=0)]
        public bool AddActivityDataResult;
        
        public AddActivityDataResponse() {
        }
        
        public AddActivityDataResponse(bool AddActivityDataResult) {
            this.AddActivityDataResult = AddActivityDataResult;
        }
    }
    
    [System.CodeDom.Compiler.GeneratedCodeAttribute("System.ServiceModel", "4.0.0.0")]
    public interface GoAberWSSoapChannel : GoAber.WebServiceClientSandbox.Consumer.GoAberWS.GoAberWSSoap, System.ServiceModel.IClientChannel {
    }
    
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.CodeDom.Compiler.GeneratedCodeAttribute("System.ServiceModel", "4.0.0.0")]
    public partial class GoAberWSSoapClient : System.ServiceModel.ClientBase<GoAber.WebServiceClientSandbox.Consumer.GoAberWS.GoAberWSSoap>, GoAber.WebServiceClientSandbox.Consumer.GoAberWS.GoAberWSSoap {
        
        public GoAberWSSoapClient() {
        }
        
        public GoAberWSSoapClient(string endpointConfigurationName) : 
                base(endpointConfigurationName) {
        }
        
        public GoAberWSSoapClient(string endpointConfigurationName, string remoteAddress) : 
                base(endpointConfigurationName, remoteAddress) {
        }
        
        public GoAberWSSoapClient(string endpointConfigurationName, System.ServiceModel.EndpointAddress remoteAddress) : 
                base(endpointConfigurationName, remoteAddress) {
        }
        
        public GoAberWSSoapClient(System.ServiceModel.Channels.Binding binding, System.ServiceModel.EndpointAddress remoteAddress) : 
                base(binding, remoteAddress) {
        }
        
        public string HelloWorld() {
            return base.Channel.HelloWorld();
        }
        
        public System.Threading.Tasks.Task<string> HelloWorldAsync() {
            return base.Channel.HelloWorldAsync();
        }
        
        [System.ComponentModel.EditorBrowsableAttribute(System.ComponentModel.EditorBrowsableState.Advanced)]
        GoAber.WebServiceClientSandbox.Consumer.GoAberWS.AddActivityDataResponse GoAber.WebServiceClientSandbox.Consumer.GoAberWS.GoAberWSSoap.AddActivityData(GoAber.WebServiceClientSandbox.Consumer.GoAberWS.AddActivityDataRequest request) {
            return base.Channel.AddActivityData(request);
        }
        
        public bool AddActivityData(GoAber.WebServiceClientSandbox.Consumer.GoAberWS.AuthHeader AuthHeader, GoAber.WebServiceClientSandbox.Consumer.GoAberWS.ActivityData[] data) {
            GoAber.WebServiceClientSandbox.Consumer.GoAberWS.AddActivityDataRequest inValue = new GoAber.WebServiceClientSandbox.Consumer.GoAberWS.AddActivityDataRequest();
            inValue.AuthHeader = AuthHeader;
            inValue.data = data;
            GoAber.WebServiceClientSandbox.Consumer.GoAberWS.AddActivityDataResponse retVal = ((GoAber.WebServiceClientSandbox.Consumer.GoAberWS.GoAberWSSoap)(this)).AddActivityData(inValue);
            return retVal.AddActivityDataResult;
        }
        
        [System.ComponentModel.EditorBrowsableAttribute(System.ComponentModel.EditorBrowsableState.Advanced)]
        System.Threading.Tasks.Task<GoAber.WebServiceClientSandbox.Consumer.GoAberWS.AddActivityDataResponse> GoAber.WebServiceClientSandbox.Consumer.GoAberWS.GoAberWSSoap.AddActivityDataAsync(GoAber.WebServiceClientSandbox.Consumer.GoAberWS.AddActivityDataRequest request) {
            return base.Channel.AddActivityDataAsync(request);
        }
        
        public System.Threading.Tasks.Task<GoAber.WebServiceClientSandbox.Consumer.GoAberWS.AddActivityDataResponse> AddActivityDataAsync(GoAber.WebServiceClientSandbox.Consumer.GoAberWS.AuthHeader AuthHeader, GoAber.WebServiceClientSandbox.Consumer.GoAberWS.ActivityData[] data) {
            GoAber.WebServiceClientSandbox.Consumer.GoAberWS.AddActivityDataRequest inValue = new GoAber.WebServiceClientSandbox.Consumer.GoAberWS.AddActivityDataRequest();
            inValue.AuthHeader = AuthHeader;
            inValue.data = data;
            return ((GoAber.WebServiceClientSandbox.Consumer.GoAberWS.GoAberWSSoap)(this)).AddActivityDataAsync(inValue);
        }
    }
}
