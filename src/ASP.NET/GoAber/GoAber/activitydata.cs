//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated from a template.
//
//     Manual changes to this file may cause unexpected behavior in your application.
//     Manual changes to this file will be overwritten if the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

namespace GoAber
{
    using System;
    using System.Collections.Generic;
    
    public partial class activitydata
    {
        public int idActivityData { get; set; }
        public int categoryUnitId { get; set; }
        public int userId { get; set; }
        public Nullable<int> value { get; set; }
        public Nullable<System.DateTime> lastUpdated { get; set; }
        public Nullable<System.DateTime> date { get; set; }
    
        public virtual categoryunit categoryunit { get; set; }
        public virtual user user { get; set; }
    }
}
