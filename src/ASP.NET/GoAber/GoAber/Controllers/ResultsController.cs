using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Linq;
using System.Threading.Tasks;
using System.Net;
using System.Web;
using System.Web.Mvc;
using GoAber.Models;

namespace GoAber.Controllers
{
    public class ResultsController : Controller
    {
        private ApplicationDbContext db = new ApplicationDbContext();

        // GET: Results
        public async Task<ActionResult> Index()
        {
            var results = db.Results.Include(r => r.categoryUnit).Include(r => r.challenge).Include(r => r.community);
            return View(await results.ToListAsync());
        }

        // GET: Results/Details/5
        public async Task<ActionResult> Details(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            Result result = await db.Results.FindAsync(id);
            if (result == null)
            {
                return HttpNotFound();
            }
            return View(result);
        }

        // GET: Results/Create
        public ActionResult Create()
        {
            ViewBag.categoryUnitId = new SelectList(db.CategoryUnits, "Id", "Id");
            ViewBag.challengeId = new SelectList(db.Challenges, "Id", "name");
            ViewBag.communityId = new SelectList(db.Communities, "Id", "name");
            return View();
        }

        // POST: Results/Create
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<ActionResult> Create([Bind(Include = "Id,categoryUnitId,value,challengeId,communityId")] Result result)
        {
            if (ModelState.IsValid)
            {
                db.Results.Add(result);
                await db.SaveChangesAsync();
                return RedirectToAction("Index");
            }

            ViewBag.categoryUnitId = new SelectList(db.CategoryUnits, "Id", "Id", result.categoryUnitId);
            ViewBag.challengeId = new SelectList(db.Challenges, "Id", "name", result.challengeId);
            ViewBag.communityId = new SelectList(db.Communities, "Id", "name", result.communityId);
            return View(result);
        }

        // GET: Results/Edit/5
        public async Task<ActionResult> Edit(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            Result result = await db.Results.FindAsync(id);
            if (result == null)
            {
                return HttpNotFound();
            }
            ViewBag.categoryUnitId = new SelectList(db.CategoryUnits, "Id", "Id", result.categoryUnitId);
            ViewBag.challengeId = new SelectList(db.Challenges, "Id", "name", result.challengeId);
            ViewBag.communityId = new SelectList(db.Communities, "Id", "name", result.communityId);
            return View(result);
        }

        // POST: Results/Edit/5
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<ActionResult> Edit([Bind(Include = "Id,categoryUnitId,value,challengeId,communityId")] Result result)
        {
            if (ModelState.IsValid)
            {
                db.Entry(result).State = EntityState.Modified;
                await db.SaveChangesAsync();
                return RedirectToAction("Index");
            }
            ViewBag.categoryUnitId = new SelectList(db.CategoryUnits, "Id", "Id", result.categoryUnitId);
            ViewBag.challengeId = new SelectList(db.Challenges, "Id", "name", result.challengeId);
            ViewBag.communityId = new SelectList(db.Communities, "Id", "name", result.communityId);
            return View(result);
        }

        // GET: Results/Delete/5
        public async Task<ActionResult> Delete(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            Result result = await db.Results.FindAsync(id);
            if (result == null)
            {
                return HttpNotFound();
            }
            return View(result);
        }

        // POST: Results/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<ActionResult> DeleteConfirmed(int id)
        {
            Result result = await db.Results.FindAsync(id);
            db.Results.Remove(result);
            await db.SaveChangesAsync();
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
