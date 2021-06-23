<!DOCTYPE HTML>
<html>

    <head>
        <link rel="shortcut icon" type="image/png" href="/static/images/favicon.ico"/>
        <script src="https://code.jquery.com/jquery-3.6.0.min.js" integrity="sha256-/xUj+3OJU5yExlq6GSYGSHk7tPXikynS7ogEvDej/m4=" crossorigin="anonymous"></script>
        <title>Sign Up</title>
        <link rel="stylesheet" href="style.css">

        <style>

        *{
            margin: 0;
            padding: 0;
            outline: 0;
            font-family: 'Open Sans', sans-serif;
        }
        body{
            height: 100vh;
            background-image: url(https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQULmNNJwmZMlmY3PuKqMtAihLXl6c-InJafg&usqp=CAU);
            background-size: cover;
            background-position: center;
            background-repeat: no-repeat;
        }

        .container{
            position: absolute;
            left: 50%;
            top: 50%;
            transform: translate(-50%,-50%);
            padding: 20px 25px;
            width: 300px;

            background-color: rgba(0,0,0,.7);
            box-shadow: 0 0 10px rgba(255,255,255,.3);
        }
        .container h1{
            text-align: left;
            color: #fafafa;
            margin-bottom: 30px;
            text-transform: uppercase;
            border-bottom: 4px solid #2979ff;
        }
        .container label{
            text-align: left;
            color: #90caf9;
        }
        .container form input{
            width: calc(100% - 20px);
            padding: 8px 10px;
            margin-bottom: 15px;
            border: none;
            background-color: transparent;
            border-bottom: 2px solid #2979ff;
            color: #fff;
            font-size: 20px;
        }
        .container form button{
            width: 100%;
            padding: 5px 0;
            border: none;
            background-color:#2979ff;
            font-size: 18px;
            color: #fafafa;
        }
        </style>

    </head>

    <body>
        <div class="container">
          <h1>Sign Up</h1>
            <form>
                <label>Username</label><br>
                <input type="text" name="name" placeholder="Create a Username" id="signup-name"> <br>
              <label>Email Address</label><br>
                <input type="email" name="name" placeholder="Email" id="signup-email"><br>

                <label>Password</label><br>
                <input type="password" name="name" placeholder="Password" id="signup-password"><br>
                <p style="color:red; display:none" id="signup-err"></p>
                <button type="button" id="btn-signup" >Signup</button>

            </form>


        <script>

            function validateSignup()
            {
                var name =$("#signup-name").val();
                var email =$("#signup-email").val();
                var password =$("#signup-password").val();

                var error="";
                if(!name){
                    error+="name invalid";
                }
                if(!email){
                    error+="email invalid";
                }
                if(!password){
                    error+="password invalid";
                }
                if(!!password && password.length<=3)
                {
                error+="password greater than 3";
                }



                $("#signup-err").html(error);
                $("#signup-err").show();
                if(error.length>0)
                    return false;

                return true;

            }
            $("#btn-signup").click(function(){

              var isValid = validateSignup();

              if(isValid)
               {
                    $("#signup-err").hide();

                    var name = $("#signup-name").val();
                    var email = $("#signup-email").val();
                    var password = $("#signup-password").val();

                    var user = {
                        "name":name,
                        "email":email,
                        "password":password

                    };

                    $.ajax({
                      type: "POST",
                      url: "/signup",
                      data: JSON.stringify(user),
                      success: function(response){

                       if(!!response){
                            if(response.user_created===true)
                                {
                                    alert(response.message);
                                }
                        }
                       },
                      contentType: "application/json"
                    });
               }
              else
                $("#signup-err").show();

            });
        </script>

        </div>

    </body>
</html>