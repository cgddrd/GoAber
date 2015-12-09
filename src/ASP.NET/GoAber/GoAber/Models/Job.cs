using GoAber.Scheduling;
using GoAber.Scheduling.Interfaces;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace GoAber.Models
{
    public class Job
    {
        public Job()
        {
            status_flag = true;
        }
        [MaxLength(100)]
        [Display(Name = "job_id", ResourceType = typeof(Resources.Resources))]
        public string id { get; set; }
        public string secretid { get; set; }

        [Display(Name = "job_tasktype", ResourceType = typeof(Resources.Resources))]
        public JobType tasktype { get; set; }
        [Display(Name = "job_schedtype", ResourceType = typeof(Resources.Resources))]
        public ScheduleType schedtype { get; set; }

        [Display(Name = "job_Minutes", ResourceType = typeof(Resources.Resources))]
        public int minutes { get; set; }

        public DateTimeOffset date { get; set; }

        public bool status_flag { get; set; }
    }
}