using GoAber.WebServiceClientSandbox.Consumer;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace GoAber.WebServiceClientSandbox.GUI
{

    public partial class MainForm : Form
    {
        GoAberClient io_client;
        public MainForm()
        {
            InitializeComponent();
            io_client = new GoAberClient();
            io_client.ConsumerUpdate += UpdateUI;
        }

        private void btn_go_Click(object sender, EventArgs e)
        {
            if (!string.IsNullOrWhiteSpace(tb_authtoken.Text)) {
                btn_go.Enabled = false;
                io_client.AddData(tb_authtoken.Text);
            }
        }

        delegate void UpdateResultCallback(bool ab_res);
        private void UpdateResult(bool ab_res)
        {
            try
            {
                if (this.lb_res.InvokeRequired)
                {
                    UpdateResultCallback lo_cb = new UpdateResultCallback(UpdateResult);
                    this.Invoke(lo_cb, new Object[] { ab_res });
                }
                else
                {
                    lb_res.Text = ab_res.ToString();
                    btn_go.Enabled = true;
                }
            }
            catch (Exception e)
            {

            }
        }

        private void UpdateUI(object sender, ConsumerEvent e)
        {
            UpdateResult(e.result);
        }

        private void textBox1_TextChanged(object sender, EventArgs e)
        {

        }
    }
}
