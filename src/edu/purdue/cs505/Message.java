package edu.purdue.cs505;

import java.util.Arrays;
import java.net.*;

public class Message {
    /** String value associated with this message. */
    private String contents;

    private int messageNumber;

    private String processID;

    private Header header;
    
    /** Constructor which sets the string contents to nothing. */
    public Message() { this.contents = ""; }

    public Message(String sourceIP, int sourcePort,
                   String destIP, int destPort,
                   int typeOfMessage, int thisSeqNum, String contents) {
        
         this.header = new Header(sourceIP, sourcePort, destIP, destPort,
                                  typeOfMessage, thisSeqNum);
         this.processID = header.getSeqNum()+":"+sourceIP + ":" +
             sourcePort;
         this.contents = contents;
    }

    /** Constructor which sets the contents to a string and inserts a timeout
     * value as well as the NACK value.
     *
     * @param contents string contents of the message.
     */
    public Message(String contents, Process p) {
        this.header = new Header(p.getIP(), p.getPort(),
                                 Header.NACK, System.currentTimeMillis());
        this.processID = header.getSeqNum()+":"+p.getIP() + ":" + p.getPort();
        this.contents = contents;
    } // Message()

    /** Get the entire message contents, type, seqNum, and payload.
     * @return string version of entire message.
     */
    public String getContents() {
        return contents;
    } // getMessageContents()

    /** Sets the message contents.
     * @param contents string value to set this message to.
     */
    public void setContents(String contents) {
        this.contents = contents;
    } // setMessageContents()

    /** Determines if this message is an ACK or not.
     * @return true if this message is an ACK, otherwise false
     */
    public boolean isACK() {
        return header.getTypeOfMessage() == Header.ACK;
    } // isACK()

    /** Turns this message into an ACK. */
    public void makeACK(String destIP, int destPort) {
        header.setTypeOfMessage(Header.ACK);
        header.setDestIP(destIP);
        header.setDestPort(destPort);
        contents = header.toString();
    }

    /** Turns this message into an ACK. */
    public Message makeEOT(int phase) {
        if (phase == 1)
            contents = Header.EOT1+""+
                contents.substring(1,contents.indexOf(':')+1);
        else if (phase == 2)
            contents = Header.EOT2+""+
                contents.substring(1,contents.indexOf(':')+1);
        else if (phase == 3)
            contents = Header.EOT3+""+
                contents.substring(1,contents.indexOf(':')+1);
        return this;
    } // makeEOT()
    
    /** Determines if this message is an EOT phase 1 or not.
     * @return true if this message is an EOT, otherwise false
     */
    public boolean isEOT1() {
        return contents.substring(0,2).equals(Header.EOT1) ? true : false;
    } // isEOT1()

    /** Determines if this message is an EOT phase 2 or not.
     * @return true if this message is an EOT, otherwise false
     */
    public boolean isEOT2() {
        return contents.substring(0,2).equals(Header.EOT2) ? true : false;
    } // isEOT2()

    /** Determines if this contents is an EOT phase 3 or not.
     * @return true if this message is an EOT, otherwise false
     */
    public boolean isEOT3() {
        return contents.substring(0,2).equals(Header.EOT3) ? true : false;
    } // isEOT3()

    /** Prints a message. Used for debugging. */
    public void printMsg() {
        System.out.println("["+header.toString()+":"+contents+"]");
    } // printMsg()


    /** Parses the message to just obtain the payload.
     * @return string value of the payload of this message.
     */
    public String getMessageString() {
        int start=0;
        for (int i=0; i<3; i++) 
            start = contents.indexOf(':', start) + 1;
        return contents.substring(start);
    }

    public int getMessageNumber() { return messageNumber; }
    public void setMessageNumber(int num) { messageNumber = num; }

    public int getTypeOfMessage() { return header.getTypeOfMessage(); }
    public void setTypeOfMessage(int type) { header.setTypeOfMessage(type); }

    public int getSourcePort() { return header.getSourcePort(); }
    public void setSourcePort(int port) { header.setSourcePort(port); }

    public String getSourceIP() { return header.getSourceIP(); }
    public void setSourceIP(String IP) { header.setSourceIP(IP); }

    public int getDestPort() { return header.getDestPort(); }
    public void setDestPort(int port) { header.setDestPort(port); }

    public String getDestIP() { return header.getDestIP(); }
    public void setDestIP(String IP) { header.setDestIP(IP); }

    public long getTimeout() { return header.getTimeout(); }
    public void setTimeout() { header.setTimeout(); }

    public String getProcessID() { return processID; }
    public void setProcessID(String messageID) { processID = processID; }

    public int getSeqNum() { return header.getSeqNum(); }
    public void setSeqNum(int seqNum) { header.setSeqNum(seqNum); }

    public void stripHeader() {
        int type, seq;
        type = Integer.parseInt(contents.substring(0,1));
        seq = Integer.parseInt(contents.substring(1,2));

        String splitString[] = contents.split(":");
        header = new Header(splitString[1], Integer.parseInt(splitString[2]),
                            splitString[3], Integer.parseInt(splitString[4]),
                            type, seq);

        StringBuffer tempContents = new StringBuffer();
        for (int i=5; i<splitString.length; i++)
            tempContents.append(splitString[i]);
        contents = tempContents.toString();
        processID = seq+":"+splitString[1]+":"+splitString[2];
    }
    
    public void toSend(String destIP, int destPort) {
        header.setDestIP(destIP);
        header.setDestPort(destPort);
    }

    public String headerToString() { return header.toString(); }
}
