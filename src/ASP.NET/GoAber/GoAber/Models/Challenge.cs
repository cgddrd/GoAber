//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated from a template.
//
//     Manual changes to this file may cause unexpected behavior in your application.
//     Manual changes to this file will be overwritten if the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

namespace GoAber.Models
{
    using System;
    using System.Collections.Generic;
    
    public partial class Challenge
    {
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2214:DoNotCallOverridableMethodsInConstructors")]
        public Challenge()
        {
            this.userchallenges = new HashSet<UserChallenge>();
            this.groupchallenges = new HashSet<GroupChallenge>();
            this.communityChallenges = new HashSet<CommunityChallenge>();
        }
    
        public int Id { get; set; }
        public int categoryUnitId { get; set; }
        public virtual CategoryUnit categoryUnit { get; set; }
        public System.DateTime startTime { get; set; }
        public Nullable<System.DateTime> endTime { get; set; }
        public string name { get; set; }
       // public Nullable<int> communityStartedBy { get; set; }
    
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2227:CollectionPropertiesShouldBeReadOnly")]
        public virtual ICollection<UserChallenge> userchallenges { get; set; }
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2227:CollectionPropertiesShouldBeReadOnly")]
        public virtual ICollection<GroupChallenge> groupchallenges { get; set; }
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2227:CollectionPropertiesShouldBeReadOnly")]
        public virtual ICollection<CommunityChallenge> communityChallenges { get; set; }

    }
}
