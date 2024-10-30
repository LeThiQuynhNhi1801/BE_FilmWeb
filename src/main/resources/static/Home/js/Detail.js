var rating=0;
var watchedTime=0;
if(localStorage.getItem("Token")){

    GetHistoryByChapterIDAndUserLogin();
    GetChapterListByChapterID();
}
else{
    window.location="/Login";
}

function GetHistoryByChapterIDAndUserLogin(){
    const xhttp = new XMLHttpRequest();
    xhttp.onload = function() 
    {
    console.log(xhttp.status);
        if(xhttp.status==200)
        {

            var historyUserJson=xhttp.responseText
            var historyUser= JSON.parse(historyUserJson)

            rating=historyUser.rate;

            watchedTime=historyUser.watchedTime;
            document.querySelectorAll('.star').forEach(function(star) {
               
                var children = star.parentNode.children;
               
                for(let i=0; i<children.length; i++) {
                    if(i<rating) {
                    children[i].classList.add('active');
                    } else {
                    children[i].classList.remove('active');
                    }
                }
            });
           
        }else if(xhttp.status=404){
            PostHistoryByChapterIDAndUserLogin();
        }
        else if(xhttp.status=401)
        {
            localStorage.removeItem("Token");
            window.location="/Login";
        }
        else if(xhttp.status=403)
        {
            localStorage.removeItem("Token");
            window.location="/Login";
        }
    }         
    //khai báo phương thức và đường dẫn để request
    xhttp.open("GET", "/ApiV1/HistoryByChapterIDAndUserLogin/"+window.location.pathname.substring(13),false);
    //định dạng gửi đi787
    xhttp.setRequestHeader("Content-type","application/json")
    token = localStorage.getItem("Token");
    authorization ='Bearer '+token
    xhttp.setRequestHeader("Authorization",authorization);
    xhttp.send();
}  
function GetChapterListByChapterID(){
    const xhttp = new XMLHttpRequest();
    xhttp.onload = function() 
    {
        if(xhttp.status==200)
        {
            var filmJson=xhttp.responseText
            var film= JSON.parse(filmJson)
            
            var CategoryList=film.chapters;
            var CategoryFilmElement = document.getElementById('videoChapter');
            var CategoryFilmNameElement = document.getElementById('nameChapterSetup');
            var CategoryFilmNameHtml='';
            var CategoryFilmListElement = document.getElementById('listChapterFilm__container');
            var CategoryFilmListHtml='<div class="listChapterFilm__header"><h1>'+film.filmName+'</h1></div><div class="listChapterFilm__body"><ul class="listChapter">';
            var CategoryFilmHtml='';
            for(var i = 0;i<CategoryList.length;i++)
            {
                CategoryFilmListHtml+='<li ';

                
                if(CategoryList[i].id== parseInt(window.location.pathname.substring(13))){
                    CategoryFilmListHtml+='class="selectedChapter"';
                    CategoryFilmHtml+=' <video src="'+CategoryList[i].video+'" autoplay ></video>';
                    CategoryFilmNameHtml+='<p>Tập'+CategoryList[i].chapterNumber+' :<span>'+CategoryList[i].chapterName+'</span></p>';
                }
                CategoryFilmListHtml+='><div class="ChapterDetail__header"><p>'+(i+1)+' <span>'+CategoryList[i].chapterName+'</span></p></div><div class="ChapterDetail__content"><div class="ChapterDetail__content__img"><a href="/DetailVideo/'+CategoryList[i].id+'"><img src="'+CategoryList[i].chapterImage+'" alt=""></a></div><div class="ChapterDetail__content__Des"><p>'+CategoryList[i].chapterDescription+'</p></div></div></li>';
            }
            CategoryFilmListHtml+='</ul></div>';
            CategoryFilmElement.innerHTML = CategoryFilmHtml;
            console.log(watchedTime);
            CategoryFilmElement.querySelector('video').currentTime=watchedTime;
            CategoryFilmNameElement.innerHTML=CategoryFilmNameHtml;
            CategoryFilmListElement.innerHTML=CategoryFilmListHtml;
        }
        else if(xhttp.status==401)
        {
            localStorage.removeItem("Token");
            window.location="/Login";
        }
        else if(xhttp.status==403)
        {
            localStorage.removeItem("Token");
            window.location="/Login";
        }
    }         
    //khai báo phương thức và đường dẫn để request
    xhttp.open("GET", "/ApiV1/FilmChapterIDChapterID/"+window.location.pathname.substring(13),false);
    //định dạng gửi đi787
    xhttp.setRequestHeader("Content-type","application/json")
    token = localStorage.getItem("Token");
    authorization ='Bearer '+token
    xhttp.setRequestHeader("Authorization",authorization);
    xhttp.send();
}    
function PostHistoryByChapterIDAndUserLogin(){
    const xhttp = new XMLHttpRequest();
    xhttp.onload = function() 
    {
        if(xhttp.status==201)
        {
           
        }
        else if(xhttp.status=401)
        {
            localStorage.removeItem("Token");
            window.location="/Login";
        }
        else if(xhttp.status=403)
        {
            localStorage.removeItem("Token");
            window.location="/Login";
        }
    }         

    xhttp.open("POST", "/ApiV1/HistoryByChapterIDAndUserLogin/"+window.location.pathname.substring(13),false);
    //định dạng gửi đi787
    xhttp.setRequestHeader("Content-type","application/json")
    token = localStorage.getItem("Token");
    authorization ='Bearer '+token
    xhttp.setRequestHeader("Authorization",authorization);
    xhttp.send();
}  
function ChangeRateHistoryByChapterIDAndUserLogin(){
    
    const xhttp = new XMLHttpRequest();
    xhttp.onload = function() 
    {
        if(xhttp.status==200)
        {
           
        }
        else if(xhttp.status=401)
        {
            localStorage.removeItem("Token");
            window.location="/Login";
        }
        else if(xhttp.status=403)
        {
            localStorage.removeItem("Token");
            window.location="/Login";
        }
    }         
    const historydata ={
        rate:rating
    }
    historydataJson = JSON.stringify(historydata);
    xhttp.open("PATCH", "/ApiV1/HistoryByChapterIDAndUserLogin/"+window.location.pathname.substring(13),false);
    //định dạng gửi đi787
    xhttp.setRequestHeader("Content-type","application/json")
    token = localStorage.getItem("Token");
    authorization ='Bearer '+token
    xhttp.setRequestHeader("Authorization",authorization);
    xhttp.send(historydataJson);
}  
function ChangewatchedTimeHistoryByChapterIDAndUserLogin(){
    
    const xhttp = new XMLHttpRequest();
    xhttp.onload = function() 
    {
        if(xhttp.status==200)
        {
           
        }
        else if(xhttp.status=401)
        {
            localStorage.removeItem("Token");
            window.location="/Login";
        }
        else if(xhttp.status=403)
        {
            localStorage.removeItem("Token");
            window.location="/Login";
        }
    }         
    const historydata ={
        watchedTime:watchedTime
    }
    historydataJson = JSON.stringify(historydata);
    xhttp.open("PATCH", "/ApiV1/HistoryByChapterIDAndUserLogin/"+window.location.pathname.substring(13),false);
    //định dạng gửi đi787
    xhttp.setRequestHeader("Content-type","application/json")
    token = localStorage.getItem("Token");
    authorization ='Bearer '+token
    xhttp.setRequestHeader("Authorization",authorization);
    xhttp.send(historydataJson);
}  
//rating event
const stars = document.querySelectorAll('.star');

stars.forEach(function(star) {
star.addEventListener('click', setclickRating);
star.addEventListener('mouseover', setRating);
star.addEventListener('mouseout', outRating);
});

function setclickRating(e) {
const value = parseInt(e.target.getAttribute('data-value'));
rating=e.target.getAttribute('data-value');
const parent = e.target.parentNode;
const children = parent.children;
for(let i=0; i<children.length; i++) {
    if(i<value) {
    children[i].classList.add('active');
    } else {
    children[i].classList.remove('active');
    }
}

ChangeRateHistoryByChapterIDAndUserLogin();
}
function setRating(e) {
    const value = parseInt(e.target.getAttribute('data-value'));

    const parent = e.target.parentNode;
    const children = parent.children;

    for(let i=0; i<children.length; i++) {
    if(i<value) {
        children[i].classList.add('active');
    } else {
        children[i].classList.remove('active');
    }
    }
}
function outRating(e) {
    const value = parseInt(e.target.getAttribute('data-value'));
    const parent = e.target.parentNode;
    const children = parent.children;
    
    for(let i=0; i<children.length; i++) {
        if(rating-1<i){
        children[i].classList.remove('active');
        }
        else{
            children[i].classList.add('active');
        }
    }
}
//video load
var videoTimeNow = document.querySelector('.video__option__timeLoad .video__option__timeNow');
var video = document.querySelector('.container__video video');
var videoTime = document.querySelector('.container__video__option .video__option__time');
checkTimeLoad(video.buffered.length);
function checkTimeLoad(buffere)
{
    
    if(buffere>0)
    {
        document.getElementById('loading').style.display='none';
    }
    else{
        document.getElementById('loading').style.display='block';
    }
}
var videoTimeLoad =document.querySelector('.video__option__time .video__option__timeLoad');
video.addEventListener('progress',()=>
{
    if(video.buffered.length>0)
    {

        videoTimeLoad.style.width = (video.buffered.end(video.buffered.length-1)/video.duration)*video.videoWidth +'px';
    }
    
})
video.addEventListener('timeupdate',()=>{
    console.log(watchedTime)
    if(Math.abs(video.currentTime-watchedTime)>=2){
        watchedTime=video.currentTime;
        ChangewatchedTimeHistoryByChapterIDAndUserLogin();
    }
   
    checkTimeLoad(video.buffered.length);
    if(video.currentTime===video.duration){
        document.querySelector('.video__listIcon__icon.pause-play').innerHTML='<i class="icon-pause"><i>';
    }
    videoTimeNow.style.width = (video.currentTime/video.duration)*videoTime.offsetWidth +'px';
   
})

videoTime.addEventListener('click',(event)=>
{
    var element = event.target || event.srcElement;
    
    var b=event.clientX/videoTime.offsetWidth*video.duration;
    video.currentTime=b;
   
});

document.querySelector('.video__listIcon__icon.pause-play').addEventListener("click",()=>
{
    var element =document.querySelector('.video__listIcon__icon.pause-play');
    if(video.paused)
    {
        video.play();
        element.innerHTML='<div class="icon__detail"><i class="icon-play"><i></div>';
    }
    else{
        video.pause();
        element.innerHTML='<div class="icon__detail"><i class="icon-pause"><i></div>';
    }
})
document.querySelector('.video__listIcon__icon.prevTenSecond').addEventListener('click',()=>{
    if(video.currentTime<10){
        video.currentTime=0;
    }else{
        video.currentTime-=10;
    }
})
document.querySelector('.video__listIcon__icon.nextTenSecond').addEventListener('click',()=>{
    if(video.duration -video.currentTime<10){
        video.currentTime=video.duration;
    }else{
        video.currentTime+=10;
    }
})

document.querySelector(".video__listIcon__icon.fullScreen").addEventListener('click',()=>{
    var bodyAll=document.querySelector('#container .container__body');
    console.log(bodyAll.requestFullscreen);
    if(document.fullscreenElement !== bodyAll){
        bodyAll.requestFullscreen();
        var element = event.target || event.srcElement;
        element.parentElement.innerHTML='<div class="icon__detail"><i class="icon-smallScreen"><i></div>';
    }else{

        document.exitFullscreen();
        var element = event.target || event.srcElement;
        element.parentElement.innerHTML='<div class="icon__detail"><i class="icon-fullScreen"><i></div>';
    }        
})
var volumn=document.querySelector(".video__listIcon__icon.volumn .icon__detail");
volumn.addEventListener('click',()=>{
    
    
    if(video.muted)
    {
        video.muted=false;
        volumn.innerHTML='<i class="icon-volume-up-1"><i>';
    }else{
        video.muted=true;
        volumn.innerHTML='<i class="icon-volume-off-1"><i>';
    }
})
var setupVolume=document.querySelector(".video__listIcon__icon.volumn .volumn__setup .volumn__setup__volumnAll");
setupVolume.addEventListener("mousedown",()=>{
    function mouseMove(){
        console.log(volumn.getBoundingClientRect().top- document.querySelector('#container .container__body').getBoundingClientRect().top);
        console.log(event.pageY);
    }
    setupVolume.addEventListener("mousemove",mouseMove)
    setupVolume.addEventListener("mouseup",()=>{
        setupVolume.removeEventListener("mousemove",mouseMove);
    })
}
)
var listChapter=document.querySelectorAll(".listChapterFilm__body .listChapter>li");
listChapter.forEach((Chapter, select) =>{
   Chapter.addEventListener('click',()=>{
    Chapter.querySelector('.ChapterDetail__content').style.display='flex';
    listChapter.forEach((ChapterClearSelect, index) =>{
        if(select!=index){
            ChapterClearSelect.querySelector('.ChapterDetail__content').style.display='none';
        }
    })
   })
  });