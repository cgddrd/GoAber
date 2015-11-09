using Microsoft.Owin;
using Owin;

[assembly: OwinStartupAttribute(typeof(GoAber.Startup))]
namespace GoAber
{
    public partial class Startup
    {
        public void Configuration(IAppBuilder app)
        {
            ConfigureAuth(app);
        }
    }
}
