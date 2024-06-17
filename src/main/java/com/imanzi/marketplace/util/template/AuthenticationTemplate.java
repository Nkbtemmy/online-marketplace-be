package com.imanzi.marketplace.util.template;

import com.imanzi.marketplace.model.User;

public class AuthenticationTemplate {

    public static String generateAccountVerificationHtml(User data, String verificationLink) {
        String displayName = data.getFirstname() != null ? data.getFirstname() : data.getUsername();
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"utf-8\" />\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />\n" +
                "    <meta name=\"description\" content=\"\" />\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\" />\n" +
                "    <link rel=\"preconnect\" href=\"https://fonts.googleapis.com\" />\n" +
                "    <link rel=\"preconnect\" href=\"https://fonts.gstatic.com\" crossorigin />\n" +
                "    <link href=\"https://fonts.googleapis.com/css2?family=Work+Sans&display=swap\" rel=\"stylesheet\" />\n" +
                "    <style>\n" +
                "        body {\n" +
                "            margin: 0;\n" +
                "            font-family: 'Work Sans', sans-serif;\n" +
                "        }\n" +
                "        .main {\n" +
                "            background-color: #fdf5f2;\n" +
                "            max-width: 650px;\n" +
                "            height: 450px;\n" +
                "            border-radius: 8px;\n" +
                "            padding-bottom: 20px;\n" +
                "            margin-left: auto;\n" +
                "            margin-right: auto;\n" +
                "            margin-top: 5%;\n" +
                "        }\n" +
                "        .header {\n" +
                "            background-color: skyblue;\n" +
                "            height: 200px;\n" +
                "            padding-top: 40px;\n" +
                "            border-radius: 8px 8px 0 0;\n" +
                "            background-image: url('https://img.freepik.com/free-photo/artistic-blurry-colorful-wallpaper-background_58702-8248.jpg');\n" +
                "            background-size: 100% 100%;\n" +
                "        }\n" +
                "        .content {\n" +
                "            background-color: #ffffff;\n" +
                "            border-radius: 8px;\n" +
                "            width: 85%;\n" +
                "            margin: auto;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        .content p {\n" +
                "            font-style: normal;\n" +
                "            font-weight: 400;\n" +
                "            line-height: 24px;\n" +
                "            font-size: 16px;\n" +
                "            padding: 5px 0 15px;\n" +
                "            color: #474d66;\n" +
                "            width: 400px;\n" +
                "            margin: auto;\n" +
                "            text-align: start;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"main\">\n" +
                "        <div class=\"header\">\n" +
                "            <div class=\"content\">\n" +
                "                <div class=\"header2\" style=\"max-width: 600px; padding: 30px 0; border-bottom: 1px solid rgb(192, 189, 195);\">\n" +
                "                    <div>MarketPlace ltd</div>\n" +
                "                </div>\n" +
                "                <p>Dear " + displayName + ",</p>\n" +
                "                <p>Thank you for registering with Marketplace platform. Please click the link below to verify your email address and complete your registration:</p>\n" +
                "                <p><a href=\"" + verificationLink + "\" style=\"color: #1a73e8;\">Verify your email</a></p>\n" +
                "                <p>If you did not create an account with us, please ignore this email.</p>\n" +
                "                <p>If you have any questions or concerns, please contact us at support@marketplace.com.</p>\n" +
                "                <p>Thank you for choosing our service.</p>\n" +
                "                <p>Best regards,<br />Marketplace Ltd</p>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }
}
