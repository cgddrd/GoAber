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
<<<<<<< HEAD:src/ASP.NET/GoAber/GoAber/device.cs
    using System.ComponentModel.DataAnnotations;

    public partial class device
=======
    
    public partial class Device
>>>>>>> develop:src/ASP.NET/GoAber/GoAber/Models/Device.cs
    {
        public int Id { get; set; }
        public int deviceTypeId { get; set; }
        public int userId { get; set; }
        [MaxLength(450)]
        public string accessToken { get; set; }
        [MaxLength(450)]
        public string refreshToken { get; set; }
        public Nullable<System.DateTime> tokenExpiration { get; set; }
    
        public virtual DeviceType deviceType { get; set; }
        public virtual User user { get; set; }
    }
}
