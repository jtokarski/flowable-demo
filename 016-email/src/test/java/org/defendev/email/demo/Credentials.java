package org.defendev.email.demo;



public class Credentials {

    public static final String GMAIL_USERNAME = "aaaaa.aaaaaaaa@gmail.com";

    /*
     * Since I have 2FA enabled on my Gmail account I can't use the primary password to my google account
     * to send e-mails through SMTP.
     * Instead, I have generated an App Password from Google Account security settings. The format
     * is strange - only lowercase letters and contains spaces "aaaa aaaa aaaa aaaa" but turns out
     * to work fine.
     *
     */
    public static final String GMAIL_PASSWORD = "bbbb cccc dddd eeee";

    public static final String GMAIL_FROM = "aaaaa.aaaaaaaa@gmail.com";

    public static final String GMAIL_TO = "qq.aaaaaaaa@zzzz.pl";

}
