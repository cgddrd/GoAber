using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Linq;
using System.Net;
using System.Web;
using System.Web.Mvc;

namespace GoAber
{
    public class devicetypesController : Controller
    {
        private goaberEntities db = new goaberEntities();

        // GET: devicetypes
        public ActionResult Index()
        {
            return View(db.devicetypes.ToList());
        }

        // GET: devicetypes/Details/5
        public ActionResult Details(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            devicetype devicetype = db.devicetypes.Find(id);
            if (devicetype == null)
            {
                return HttpNotFound();
            }
            return View(devicetype);
        }

        // GET: devicetypes/Create
        public ActionResult Create()
        {
            return View();
        }

        // POST: devicetypes/Create
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Create([Bind(Include = "idDeviceType,name,tokenEndpoint,consumerKey,consumerSecret,clientId,authorizationEndpoint")] devicetype devicetype)
        {
            if (ModelState.IsValid)
            {
                db.devicetypes.Add(devicetype);
                db.SaveChanges();
                return RedirectToAction("Index");
            }

            return View(devicetype);
        }

        // GET: devicetypes/Edit/5
        public ActionResult Edit(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            devicetype devicetype = db.devicetypes.Find(id);
            if (devicetype == null)
            {
                return HttpNotFound();
            }
            return View(devicetype);
        }

        // POST: devicetypes/Edit/5
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Edit([Bind(Include = "idDeviceType,name,tokenEndpoint,consumerKey,consumerSecret,clientId,authorizationEndpoint")] devicetype devicetype)
        {
            if (ModelState.IsValid)
            {
                db.Entry(devicetype).State = EntityState.Modified;
                db.SaveChanges();
                return RedirectToAction("Index");
            }
            return View(devicetype);
        }

        // GET: devicetypes/Delete/5
        public ActionResult Delete(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            devicetype devicetype = db.devicetypes.Find(id);
            if (devicetype == null)
            {
                return HttpNotFound();
            }
            return View(devicetype);
        }

        // POST: devicetypes/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public ActionResult DeleteConfirmed(int id)
        {
            devicetype devicetype = db.devicetypes.Find(id);
            db.devicetypes.Remove(devicetype);
            db.SaveChanges();
            return RedirectToAction("Index");
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }
    }
}
