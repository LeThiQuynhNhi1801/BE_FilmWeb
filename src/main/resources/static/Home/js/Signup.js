
var userNameInput=document.getElementById("Username");
var passwordInput=document.getElementById("password");
var fullNameInput=document.getElementById("fullName");
var labelUserName = document.querySelector("label[for='" + userNameInput.id + "']");
var labelPassword = document.querySelector("label[for='" + passwordInput.id + "']");
var labelFullName = document.querySelector("label[for='" + fullNameInput.id + "']");
if(passwordInput.value==''){

    labelUserName.style.fontSize="11px";
    labelUserName.style.top="7px";
}
if(userNameInput.value=='')
{
    labelPassword.style.fontSize="11px";
    labelPassword.style.top="7px";
}
if(fullNameInput.value=='')
{
    labelFullName.style.fontSize="11px";
    labelFullName.style.top="7px";
}
userNameInput.addEventListener("focus",() => {
    var userNameError=document.querySelector(".username__Error.Error");
    var signupinforError = document.querySelector('#signup__Eror');
    userNameError.style.display="none";
    signupinforError.style.display="none";
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
        var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/; 
        if (!filter.test(userNameInput.value)) { 
            userNameError.innerHTML="Vui lòng nhập email hoặc số điện thoại hợp lệ.";
            userNameError.style.display="block";
            userNameInput.style.borderBottom ="2px solid #e87c03";
        }
    }

});
passwordInput.addEventListener("focus",() => {
    
    var passwordError=document.querySelector(".password__Error.Error");
    var signupinforError =document.querySelector('#signup__Eror');
    passwordError.style.display="none";
    signupinforError.style.display="none";
    passwordInput.style.borderBottom ="none";
    labelPassword.style.fontSize="11px";
    labelPassword.style.top="7px";
});
passwordInput.addEventListener("blur",() => {
    
    var passwordError=document.querySelector(".password__Error.Error");
    
    if(passwordInput.value==''){
        passwordError.innerHTML="Mật khẩu của bạn phải chứa từ 4 đến 60 ký tự.";
        passwordError.style.display="block";
        passwordInput.style.borderBottom ="2px solid #e87c03";
        labelPassword.style.fontSize="16px";
        labelPassword.style.top="23%";
    }else if(passwordInput.value.length<4 ){
        passwordError.innerHTML="Mật khẩu của bạn phải chứa từ 4 đến 60 ký tự.";
        passwordError.style.display="block";
        passwordInput.style.borderBottom ="2px solid #e87c03";
    }
    else{
       /* userNameError.innerHTML="Vui lòng nhập email hoặc số điện thoại hợp lệ.";
        userNameError.style.display="block";*/
    }

});
fullNameInput.addEventListener("focus",() => {
    
    var fullNameError=document.querySelector(".fullName__Error.Error");
    var signupinforError =document.querySelector('#signup__Eror');
    fullNameError.style.display="none";
    signupinforError.style.display="none";
    fullNameInput.style.borderBottom ="none";
    labelFullName.style.fontSize="11px";
    labelFullName.style.top="7px";
});
fullNameInput.addEventListener("blur",() => {
    
    var fullNameError=document.querySelector(".fullName__Error.Error");
    
    if(fullNameInput.value==''){
        fullNameError.innerHTML="Mật khẩu của bạn phải chứa từ 10 đến 60 ký tự.";
        fullNameError.style.display="block";
        fullNameInput.style.borderBottom ="2px solid #e87c03";
        labelFullName.style.fontSize="16px";
        labelFullName.style.top="23%";
    }else if(fullNameInput.value.length<10 ){
        fullNameError.innerHTML="Mật khẩu của bạn phải chứa từ 10 đến 60 ký tự.";
        fullNameError.style.display="block";
        fullNameInput.style.borderBottom ="2px solid #e87c03";
    }
    else{
       /* userNameError.innerHTML="Vui lòng nhập email hoặc số điện thoại hợp lệ.";
        userNameError.style.display="block";*/
    }

});
function signupByEnter(){
    if(event.keyCode==13){
        signup();
    }
}
function signup()
{
    const xhttp = new XMLHttpRequest();
    xhttp.onload = function() 
    {

        if(xhttp.status==201)
        {
             var tokenResponseJson=xhttp.responseText
               var tokenResponse= JSON.parse(tokenResponseJson)
            window.location='/Login';
            
        }
        else
        {
            if(tokenResponse['message']){
                var userNameError=document.querySelector(".username__Error.Error");
                userNameError.innerHTML=tokenResponse['UserName'];
                userNameError.style.display="block";
                userNameInput.style.borderBottom ="2px solid #e87c03";
            };
            document.querySelector('#signup__Eror').innerHTML='<p>Vui lòng thử lại hoặc tạo một tài khoản mới.</p>'
            document.querySelector('#signup__Eror').style.display='block';
        }
    }         
    const userInfo={
        username:userNameInput.value,
        password:passwordInput.value,
        fullName:fullNameInput.value
    }
    postData=JSON.stringify(userInfo)
    xhttp.open("POST", "/register",false);
    xhttp.setRequestHeader("Content-type","application/json")
    xhttp.send(postData)
}