﻿using GoAber.Scheduling;
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
        [DisplayName("Unique Name")]
        public string id { get; set; }
        public string secretid { get; set; }

        [DisplayName("Task Type")]
        public JobType tasktype { get; set; }
        [DisplayName("Schedule Type")]
        public ScheduleType schedtype { get; set; }

        [DisplayName("Minutes")]
        public int minutes { get; set; }

        public DateTimeOffset date { get; set; }

        public bool status_flag { get; set; }
    }
}