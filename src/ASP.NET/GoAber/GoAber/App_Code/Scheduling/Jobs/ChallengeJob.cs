using GoAber.Models;
using GoAber.Scheduling.Interfaces;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Web;

namespace GoAber.App_Code.Scheduling.Jobs
{
    public class ChallengeJob : IJob
    {
        public string GetID()
        {
            return "challengejob";
        }

        public void Run(string[] args)
        {
           Debug.WriteLine("Challenge complete");
        }
    }
}