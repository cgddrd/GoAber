using System;
using System.Collections.Generic;
using System.Collections.Specialized;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;
using System.Web;

namespace GoAber.App_Code.OAuth
{
    [Obsolete]
    public static class ManualHTTPRequests
    {
        [Obsolete("Manual HTTP Refresh doesn't work. Believe this is to do with the Authorization header construction.")]
        public static HttpWebRequest Refresh(string ls_consumer, string ls_secret, string ls_reftoken)
        {
            List<String[]> headers = new List<String[]>();
            headers.Add(new String[] { "Authorization", "Basic " + Convert.ToBase64String(Encoding.UTF8.GetBytes(ls_consumer + ":" + ls_secret)) });

            List<String[]> formdata = new List<String[]>();
            formdata.Add(new String[] { "grant_type", "refresh_token" });
            formdata.Add(new String[] { "refresh_token", ls_reftoken });

            return CreateRequest(
                new Uri("https://api.fitbit.com/oauth2/token"),
                "POST",
                "application/x-www-form-urlencoded",
                headers,
                formdata
                );
        }

        public static HttpWebRequest CreateRequest(Uri uri, String method, String contenttype, List<String[]> headers = null, List<String[]> formdata = null, String data = "")
        {

            HttpWebRequest request = HttpWebRequest.CreateHttp(uri);
            request.Method = method;
            if (headers != null)
            {
                for (int i = 0; i < headers.Count; i++)
                {
                    request.Headers.Add(headers[i][0], headers[i][1]);
                }
            }
            if (formdata != null)
            {
                NameValueCollection outgoingQueryString = HttpUtility.ParseQueryString(String.Empty);
                for (int i = 0; i < formdata.Count; i++)
                {
                    outgoingQueryString.Add(formdata[i][0], formdata[i][1]);
                }
                string temp = outgoingQueryString.ToString();
                data += temp;
            }
            request.ContentType = contenttype;
            if (!String.IsNullOrWhiteSpace(data))
            {
                byte[] byteArray = Encoding.UTF8.GetBytes(data);
                // Set the ContentType property of the WebRequest.
                // Set the ContentLength property of the WebRequest.
                request.ContentLength = byteArray.Length;

                Stream dataStream = request.GetRequestStream();
                // Write the data to the request stream.
                dataStream.Write(byteArray, 0, byteArray.Length);
                // Close the Stream object.
                dataStream.Close();
            }

            return request;
        }

        public static String[] GetResponse(HttpWebRequest request)
        {
                String[] ret = new String[2];
                // Get the response.
                WebResponse response = request.GetResponse();
                // Display the status.
                ret[0] = ((HttpWebResponse)response).StatusDescription;
                // Get the stream containing content returned by the server.
                Stream dataStream = response.GetResponseStream();
                // Open the stream using a StreamReader for easy access.
                StreamReader reader = new StreamReader(dataStream);
                // Read the content.
                string responseFromServer = reader.ReadToEnd();
                // Display the content.
                ret[1] = responseFromServer;
                // Clean up the streams.
                reader.Close();
                dataStream.Close();
                response.Close();
                return ret;
        }
    }
}