namespace GoAber.WebServiceClientSandbox.GUI
{
    partial class MainForm
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.btn_go = new System.Windows.Forms.Button();
            this.label1 = new System.Windows.Forms.Label();
            this.lb_res = new System.Windows.Forms.Label();
            this.tb_authtoken = new System.Windows.Forms.TextBox();
            this.label2 = new System.Windows.Forms.Label();
            this.SuspendLayout();
            // 
            // btn_go
            // 
            this.btn_go.Location = new System.Drawing.Point(347, 40);
            this.btn_go.Name = "btn_go";
            this.btn_go.Size = new System.Drawing.Size(75, 23);
            this.btn_go.TabIndex = 0;
            this.btn_go.Text = "Go";
            this.btn_go.UseVisualStyleBackColor = true;
            this.btn_go.Click += new System.EventHandler(this.btn_go_Click);
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(428, 43);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(76, 17);
            this.label1.TabIndex = 1;
            this.label1.Text = "Response:";
            // 
            // lb_res
            // 
            this.lb_res.AutoSize = true;
            this.lb_res.Location = new System.Drawing.Point(511, 43);
            this.lb_res.Name = "lb_res";
            this.lb_res.Size = new System.Drawing.Size(16, 17);
            this.lb_res.TabIndex = 2;
            this.lb_res.Text = "?";
            // 
            // tb_authtoken
            // 
            this.tb_authtoken.Location = new System.Drawing.Point(94, 12);
            this.tb_authtoken.Name = "tb_authtoken";
            this.tb_authtoken.Size = new System.Drawing.Size(437, 22);
            this.tb_authtoken.TabIndex = 3;
            this.tb_authtoken.Text = "2390ade2-98cd-4aed-82b6-3f5a6a5ccb4e";
            this.tb_authtoken.TextChanged += new System.EventHandler(this.textBox1_TextChanged);
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(3, 13);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(85, 17);
            this.label2.TabIndex = 4;
            this.label2.Text = "Auth Token:";
            // 
            // MainForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 16F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(562, 79);
            this.Controls.Add(this.label2);
            this.Controls.Add(this.tb_authtoken);
            this.Controls.Add(this.lb_res);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.btn_go);
            this.Name = "MainForm";
            this.Text = "Client";
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Button btn_go;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Label lb_res;
        private System.Windows.Forms.TextBox tb_authtoken;
        private System.Windows.Forms.Label label2;
    }
}