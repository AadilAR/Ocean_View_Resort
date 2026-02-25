<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*" %>

<html>
<head>
    <title>Ocean View Resort | Reports</title>

    <style>
        body {
            font-family: "Segoe UI", Arial, sans-serif;
            background: linear-gradient(135deg, #203a43, #2c5364);
            margin: 0;
            padding: 40px;
            color: white;
        }

        h2 {
            text-align: center;
            margin-bottom: 30px;
        }

        .card {
            background: rgba(255,255,255,0.1);
            backdrop-filter: blur(10px);
            padding: 25px;
            border-radius: 15px;
            margin: 20px auto;
            width: 80%;
            box-shadow: 0 10px 25px rgba(0,0,0,0.4);
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 15px;
        }

        th, td {
            padding: 12px;
            text-align: center;
            border: 1px solid rgba(255,255,255,0.3);
        }

        th {
            background: rgba(0,0,0,0.4);
        }

        .back-btn {
            display: inline-block;
            margin-top: 30px;
            padding: 12px 20px;
            background: #1e3c50;
            color: white;
            text-decoration: none;
            border-radius: 8px;
        }

        .back-btn:hover {
            background: #163041;
        }
    </style>
</head>

<body>

<h2>Executive Report Dashboard</h2>

<%
    String type = (String) request.getAttribute("reportType");
    Object data = request.getAttribute("reportData");
%>

<div class="card">

<% if ("revenue".equals(type)) { %>

    <h3>Monthly Revenue Report</h3>

    <table>
        <tr>
            <th>Year</th>
            <th>Month</th>
            <th>Total Revenue</th>
        </tr>

        <%
            List<Map<String,Object>> revenueList =
                    (List<Map<String,Object>>) data;

            for (Map<String,Object> row : revenueList) {
        %>
        <tr>
            <td><%= row.get("year") %></td>
            <td><%= row.get("month") %></td>
            <td><%= row.get("totalRevenue") %></td>
        </tr>
        <% } %>
    </table>

<% } else if ("occupancy".equals(type)) { %>

    <h3>Monthly Occupancy Report</h3>

    <table>
        <tr>
            <th>Month</th>
            <th>Total Bookings</th>
        </tr>

        <%
            List<Map<String,Object>> occupancyList =
                    (List<Map<String,Object>>) data;

            for (Map<String,Object> row : occupancyList) {
        %>
        <tr>
            <td><%= row.get("month") %></td>
            <td><%= row.get("totalBookings") %></td>
        </tr>
        <% } %>
    </table>

<% } else if ("popular".equals(type)) { %>

    <h3>Most Booked Room</h3>

    <%
        Map<String,Object> popular =
                (Map<String,Object>) data;
    %>

    <p><strong>Room ID:</strong> <%= popular.get("roomId") %></p>
    <p><strong>Total Bookings:</strong> <%= popular.get("totalBookings") %></p>

<% } else { %>

    <p>No report selected.</p>

<% } %>

</div>

<div style="text-align:center;">
    <a href="reservation.html" class="back-btn">Back to Dashboard</a>
</div>

</body>
</html>