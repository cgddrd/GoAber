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
    
    public partial class GroupChallenge
    {
        public int Id { get; set; }
        public int groupId { get; set; }
        public int challengeId { get; set; }
    
        public virtual Challenge challenge { get; set; }
        public virtual Team team { get; set; }
    }
}