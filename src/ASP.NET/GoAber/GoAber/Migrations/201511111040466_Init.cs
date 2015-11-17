namespace GoAber.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class _20151111_Init : DbMigration
    {
        public override void Up()
        {
            AddColumn("dbo.DeviceTypes", "apiEndpoint", c => c.String());
        }
        
        public override void Down()
        {
            DropColumn("dbo.DeviceTypes", "apiEndpoint");
        }
    }
}
