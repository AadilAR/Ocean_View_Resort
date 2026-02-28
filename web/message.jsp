<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>System Message</title>

    <style>
        body {
            margin: 0;
            padding: 0;
            font-family: 'Segoe UI', Arial, sans-serif;
            height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;

            /* Background Image */
            background: url("<%= request.getContextPath() %>/images/background.jpg")
                        no-repeat center center fixed;
            background-size: cover;
        }

        /* Glass Card */
        .glass-card {
            width: 400px;
            padding: 40px;
            border-radius: 15px;

            background: rgba(255, 255, 255, 0.15);
            backdrop-filter: blur(15px);
            -webkit-backdrop-filter: blur(15px);

            border: 1px solid rgba(255, 255, 255, 0.3);
            box-shadow: 0 8px 32px rgba(0, 0, 0, 0.25);

            text-align: center;
            color: white;
        }

        .message {
            font-size: 18px;
            font-weight: 600;
            margin-bottom: 25px;
        }

        .error {
            color: #ff6b6b;
        }

        .success {
            color: #4cd964;
        }

        .btn {
            display: inline-block;
            padding: 10px 20px;
            border-radius: 25px;
            text-decoration: none;
            font-weight: 500;
            transition: 0.3s ease;
        }

        .btn-login {
            background: rgba(255,255,255,0.25);
            color: white;
            border: 1px solid rgba(255,255,255,0.4);
        }

        .btn-login:hover {
            background: rgba(255,255,255,0.4);
        }
    </style>
</head>
<body>

<%
    String message = (String) request.getAttribute("message");
    String type = (String) request.getAttribute("type");
%>

<div class="glass-card">

    <div class="message <%= type %>">
        <%= message %>
    </div>

    <a class="btn btn-login"
       href="<%= request.getContextPath() %>/login.html">
        Go to Login
    </a>

</div>

</body>
</html>