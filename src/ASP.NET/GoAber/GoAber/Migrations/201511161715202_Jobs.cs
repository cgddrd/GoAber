namespace GoAber.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class Jobs : DbMigration
    {
        public override void Up()
        {
            DropTable("dbo.Jobs");
        }
        
        public override void Down()
        {
            CreateTable(
                "dbo.Jobs",
                c => new
                    {
                        id = c.String(nullable: false, maxLength: 100),
                        secretid = c.String(),
                        tasktype = c.Int(nullable: false),
                        schedtype = c.Int(nullable: false),
                        minutes = c.Int(nullable: false),
                    })
                .PrimaryKey(t => t.id);
            
        }
    }
}
