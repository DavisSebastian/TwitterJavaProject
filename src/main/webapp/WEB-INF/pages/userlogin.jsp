<html>
    <body>
    USER LOGI!

    <div id = "time">

    </div>


    <script type = "text/javascript">

    function updateTime()
    {
        document.getElementById("time").innerText = new Date().toString();
    }

    setInterval(updateTime,1000);
    </script>
    </body>

</html>