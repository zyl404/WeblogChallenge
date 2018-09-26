import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.time.*;


public class LogEntry implements Serializable {
    private Timestamp timestamp;
    private String elb;
    private String client;
    private String backend;
    private Float requestProcessingTime;
    private Float backendProcessingTime;
    private Float responseProcessingTime;
    private Integer elbStatusCode;
    private Integer backendStatusCode;
    private Integer receivedBytes;
    private Integer sentBytes;
    private String requestMethod;
    private String url;
    private String httpVersion;
    private String userAgent;
    private String sslCipher;
    private String sslProtocol;
    
    private String buildUserAgent(List<String> strings) {
        strings.set(0, strings.get(0).replace("\"", ""));
        strings.set(strings.size()-1, strings.get(strings.size()-1).replace("\"", ""));
        
        StringJoiner stringJoiner = new StringJoiner(" ");
        for (String i : strings) {
            stringJoiner.add(i);
        }
        return stringJoiner.toString();
    }

    public LogEntry(String raw) throws ParseException {
        String[] removeSapce = raw.split(" ");
        this.timestamp = Timestamp.from(Instant.from(DateTimeFormatter.ISO_DATE_TIME.parse(removeSapce[0])));
        this.elb = removeSapce[1];
        this.client = removeSapce[2];
        this.backend = removeSapce[3];
        this.requestProcessingTime = Float.valueOf(removeSapce[4]);
        this.backendProcessingTime = Float.valueOf(removeSapce[5]);
        this.responseProcessingTime = Float.valueOf(removeSapce[6]);
        this.elbStatusCode = Integer.valueOf(removeSapce[7]);
        this.backendStatusCode = Integer.valueOf(removeSapce[8]);
        this.receivedBytes = Integer.valueOf(removeSapce[9]);
        this.sentBytes = Integer.valueOf(removeSapce[10]);
        this.requestMethod = removeSapce[11].replace("\"","");
        this.url = removeSapce[12];
        this.httpVersion = removeSapce[13];
        List<String> tmp = new ArrayList<String>();
        tmp.add(removeSapce[14]);
        int i = 15;
        if (!(removeSapce[14].startsWith("\"") && removeSapce[14].endsWith("\""))) {
            for (; i < removeSapce.length; i++) {
                tmp.add(removeSapce[i]);
                if (removeSapce[i].endsWith("\"")) {
                    break;
                }
            }
        }
        this.userAgent = buildUserAgent(tmp);
        this.sslCipher = removeSapce[i];
        this.sslProtocol = removeSapce[i+1];
    }

    public java.sql.Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(java.sql.Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getElb() {
        return elb;
    }

    public void setElb(String elb) {
        this.elb = elb;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getBackend() {
        return backend;
    }

    public void setBackend(String backend) {
        this.backend = backend;
    }

    public Float getRequestProcessingTime() {
        return requestProcessingTime;
    }

    public void setRequestProcessingTime(Float requestProcessingTime) {
        this.requestProcessingTime = requestProcessingTime;
    }

    public Float getBackendProcessingTime() {
        return backendProcessingTime;
    }

    public void setBackendProcessingTime(Float backendProcessingTime) {
        this.backendProcessingTime = backendProcessingTime;
    }

    public Float getResponseProcessingTime() {
        return responseProcessingTime;
    }

    public void setResponseProcessingTime(Float responseProcessingTime) {
        this.responseProcessingTime = responseProcessingTime;
    }

    public Integer getElbStatusCode() {
        return elbStatusCode;
    }

    public void setElbStatusCode(Integer elbStatusCode) {
        this.elbStatusCode = elbStatusCode;
    }

    public Integer getBackendStatusCode() {
        return backendStatusCode;
    }

    public Integer getReceivedBytes() {
        return receivedBytes;
    }

    public void setReceivedBytes(Integer receivedBytes) {
        this.receivedBytes = receivedBytes;
    }

    public Integer getSentBytes() {
        return sentBytes;
    }

    public void setSentBytes(Integer sentBytes) {
        this.sentBytes = sentBytes;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public void setBackendStatusCode(Integer backendStatusCode) {
        this.backendStatusCode = backendStatusCode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getSslCipher() {
        return sslCipher;
    }

    public void setSslCipher(String sslCipher) {
        this.sslCipher = sslCipher;
    }

    public String getSslProtocol() {
        return sslProtocol;
    }

    public void setSslProtocol(String sslProtocol) {
        this.sslProtocol = sslProtocol;
    }

    public static void main(String[] args) throws ParseException {
        String record = "2015-07-22T09:00:28.048369Z marketpalce-shop 180.179.213.94:48725 10.0.6.108:80 0.00002 0.002333 0.000021 200 200 0 35734 \"GET https://paytm.com:443/shop/p/micromax-yu-yureka-moonstone-grey-MOBMICROMAX-YU-DUMM141CD60AF7C_34315 HTTP/1.0\" \"-\" ECDHE-RSA-AES128-GCM-SHA256 TLSv1.2";
        LogEntry e = null;
        try {
            e = new LogEntry(record);
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        System.out.println(e.getHttpVersion());

        
    }
}
