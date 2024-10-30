
if(localStorage.getItem("Token")){
    window.location="/Home";
    // window.history.back();
 }
 else{
 
 }
     var userNameInput=document.getElementById("Username");
     var passwordInput=document.getElementById("password");
 
     var labelUserName = document.querySelector("label[for='" + userNameInput.id + "']");
     var labelPassword = document.querySelector("label[for='" + passwordInput.id + "']");
     if(passwordInput.value==''){
 
         labelUserName.style.fontSize="11px";
         labelUserName.style.top="7px";
     }
     if(userNameInput.value=='')
     {
         labelPassword.style.fontSize="11px";
         labelPassword.style.top="7px";
     }
     userNameInput.addEventListener("focus",() => {
         var userNameError=document.querySelector(".username__Error.Error");
         var logininforError = document.querySelector('#Login__Eror');
         userNameError.style.display="none";
         logininforError.style.display="none";
         userNameInput.style.borderBottom ="none";
         labelUserName.style.fontSize="11px";
         labelUserName.style.top="7px";
     });
     userNameInput.addEventListener("blur",() => {
         var userNameError=document.querySelector(".username__Error.Error");
         
         if(userNameInput.value==''){
             userNameError.innerHTML="Vui lòng nhập username";
             userNameError.style.display="block";
             userNameInput.style.borderBottom ="2px solid #e87c03";
             labelUserName.style.fontSize="16px";
             labelUserName.style.top="23%";
             
         }
         else{
            /* userNameError.innerHTML="Vui lòng nhập email hoặc số điện thoại hợp lệ.";
             userNameError.style.display="block";*/
         }
 
     });
     passwordInput.addEventListener("focus",() => {
         
         var passwordError=document.querySelector(".password__Error.Error");
         var logininforError =document.querySelector('#Login__Eror');
         passwordError.style.display="none";
         logininforError.style.display="none";
         passwordInput.style.borderBottom ="none";
         labelPassword.style.fontSize="11px";
         labelPassword.style.top="7px";
     });
     passwordInput.addEventListener("blur",() => {
         
         var passwordError=document.querySelector(".password__Error.Error");
         
         if(passwordInput.value.length<4 ||passwordInput.value==''){
             passwordError.innerHTML="Mật khẩu của bạn phải chứa từ 4 đến 60 ký tự.";
             passwordError.style.display="block";
             passwordInput.style.borderBottom ="2px solid #e87c03";
             labelPassword.style.fontSize="16px";
             labelPassword.style.top="23%";
         }
         else{
            /* userNameError.innerHTML="Vui lòng nhập email hoặc số điện thoại hợp lệ.";
             userNameError.style.display="block";*/
         }
 
     });
     function loginByEnter(){
         if(event.keyCode==13){
             login();
         }
     }
     function login()
     {
         const xhttp = new XMLHttpRequest();
         xhttp.onload = function() 
         {
            
             if(xhttp.status==200)
             {
                 var tokenResponseJson=xhttp.responseText
                 var tokenResponse= JSON.parse(tokenResponseJson)
                 localStorage.setItem("Token", tokenResponse['token']);
                 window.location='/Home';
                 
             }
             else
             {

                 document.querySelector('#Login__Eror').innerHTML='<p>Rất tiếc, chúng tôi không tìm thấy tài khoản nào có địa chỉ email này. Vui lòng thử lại hoặc tạo một tài khoản mới.</p>'
                 document.querySelector('#Login__Eror').style.display='block';
             }
         }         
         const userInfo={
             username:userNameInput.value,
             password:passwordInput.value
         }
         postData=JSON.stringify(userInfo)
         xhttp.open("POST", "/auth",false);
         xhttp.setRequestHeader("Content-type","application/json")
         xhttp.send(postData)
     }