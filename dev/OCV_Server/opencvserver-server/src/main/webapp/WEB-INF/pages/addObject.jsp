<%--
  Created by IntelliJ IDEA.
  User: Jannik
  Date: 19.11.14
  Time: 17:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <style>
        .markdown-body {
            min-width: 200px;
            max-width: 790px;
            margin: 0 auto;
            padding: 30px;
        }
    </style>
    <title></title>
</head>
<body>
<article class="markdown-body">

    <form action="add" enctype="multipart/form-data" method="post">
        <h1>
            OCV Server
        </h1>

        <h4>
            This page provides the opportunity to add a new object to the server.
        </h4>

        <h4>
            Name:
        </h4>

        <p>
            <input name="name" type="text" accept="text/xml">
        </p>
        <h4>
            File:
        </h4>

        <p>
            <input name="file" type="file">
        </p>

        <p>
            <input type="submit" name="Submit">
        </p>
    </form>
</article>
</body>
</html>
