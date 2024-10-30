var check;
var historyUserLogin=[];
var prevs;
var nexts;
var demCategory=0;

if(localStorage.getItem("Token")){
    
    GetHotChapter();
    GetChapterRecommend();
    GetHistoryUserLogin();
    GetCategoryFilmList();
    
    
}
else{
    window.location="/Login";
}
function LogOut(){
    window.location="/Login";
    localStorage.removeItem("Token");
}
function GetChapterRecommend(){
    const xhttp = new XMLHttpRequest();
    xhttp.onload = function() 
    {
        
        if(xhttp.status==200)
        {
          
            var RecommendChapterJson=xhttp.responseText
            var RecommendChapter= JSON.parse(RecommendChapterJson)
            
            var RecommendChapterElement = document.getElementById('RecommendChapter');
            var RecommendChapterHtml=' <span class="prev"><i class="fa-solid fa-angle-left"></i></span>';
            for(var i = 0;i<RecommendChapter.length;i++)
            {
                RecommendChapterHtml+=' <div class="VideoList__Videos"><a href="/DetailVideo/'+RecommendChapter[i].id+'" class="Videos__video"><div class="Videos__video__content"><div class="video__content__img"><img src="'+RecommendChapter[i].chapterImage+'" alt=""></div><div class="video__content__video"><video src="'+RecommendChapter[i].trailerChapter+'" autoplay muted="true"></video></div><div class="video__content__des"><div class="content__des__body"><div class="des__body__listIcon"><div class="listIcon__icon"><i class="fa-solid fa-play"></i></div></div><div class="des__body__title"><p>'+RecommendChapter[i].chapterName+'</p></div></div></div></div></a></div>';
            }
            RecommendChapterHtml+=' <span class="next"><i class="fa-solid fa-angle-right"></i></span>';
            RecommendChapterElement.innerHTML=RecommendChapterHtml;
        }
        else if(xhttp.status==403){
            localStorage.removeItem("Token");
            window.location="/Login"
        }
    }         
    //khai báo phương thức và đường dẫn để request
    xhttp.open("GET", "/ApiV1/Recommend",false);
    //định dạng gửi đi787
    xhttp.setRequestHeader("Content-type","application/json")
    token = localStorage.getItem("Token");
    authorization ='Bearer '+token
    xhttp.setRequestHeader("Authorization",authorization);
    xhttp.send();
}
function GetHistoryUserLogin(){
    const xhttp = new XMLHttpRequest();
    xhttp.onload = function() 
    {
        var ChapterHotJson=xhttp.responseText
        var ChapterHot= JSON.parse(ChapterHotJson)
        if(xhttp.status==200)
        {
            
            ChapterHot.forEach(
                (currentValue, index) =>{
                    historyUserLogin.push(currentValue.id);
                  }
            );
            
           console.log(historyUserLogin);
            
        }
        else if(xhttp.status==401)
        {
            localStorage.removeItem("Token");
            window.location="/Login"
        }else if(xhttp.status==403){
            localStorage.removeItem("Token");
            window.location="/Login"
        }
    }         
    //khai báo phương thức và đường dẫn để request
    xhttp.open("GET", "/ApiV1/HistoryUserLogin",false);
    //định dạng gửi đi787
    xhttp.setRequestHeader("Content-type","application/json")
    token = localStorage.getItem("Token");
    authorization ='Bearer '+token
    xhttp.setRequestHeader("Authorization",authorization);
    //gửi
    xhttp.send();
}  
function GetCategoryFilmList(){
    const xhttp = new XMLHttpRequest();
    xhttp.onload = function() 
    {
        var CategoryFilmJson=xhttp.responseText
        var CategoryFilm= JSON.parse(CategoryFilmJson)
        if(xhttp.status==200)
        {
            
            check= new Array(CategoryFilm.length+1).fill(0);
            
            var CategoryFilmElement = document.getElementById('CategoryFilm');
            var CategoryFilmHtml='';
            for(var i = 0;i<CategoryFilm.length;i++)
            {   
              
                if(CategoryFilm[i].films.length!=0 )
                {
                   
                    var countFilm=0;
                    demCategory++;
                    CategoryFilmDetailHtml='<div class="ToiecList__ToiecVideos"><div class="ToiecVideos__ToiecName"><h1>'+CategoryFilm[i].categoryName+'</h1></div><div class="ToeicVideos__VideoList"><span class="prev"><i class="fa-solid fa-angle-left"></i></span> ';
                    for(var j=0;j<CategoryFilm[i].films.length;j++)
                    {
                        
                        try{
                            
                            if(CategoryFilm[i].films[j].chapters[0].chapterStatus=='Đã Ra')
                            {
                                
                                var lastHistory='';
                                
                                try{
                                    lastHistory ='<div class="VideoList__Videos"><a href="/DetailVideo/'+CategoryFilm[i].films[j].chapters[0].id+'" class="Videos__video"><div class="Videos__video__content"><div class="video__content__img"><img src="'+CategoryFilm[i].films[j].chapters[0].chapterImage+'" alt=""></div><div class="video__content__video"><video src="'+CategoryFilm[i].films[j].chapters[0].video+'" autoplay muted="true"></video></div><div class="video__content__des"><div class="content__des__body"><div class="des__body__listIcon"><div class="listIcon__icon"><i class="fa-solid fa-play"></i></div></div><div class="des__body__title"><p>'+CategoryFilm[i].films[j].chapters[0].chapterName+'</p><p>Thể loại: <span>'+CategoryFilm[i].categoryName+'</span></p></div></div></div></div></a></div>';
                                   
                                }
                                catch{
                                    lastHistory='';
                                }
                                
                                countFilm++;
                                
                                for(var k=0;k<CategoryFilm[i].films[j].chapters.length;k++)
                                {
                                      if(CategoryFilm[i].films[j].chapters[k].chapterStatus=='Đã Ra')
                                    {
                                        
                                        if(historyUserLogin.includes(CategoryFilm[i].films[j].chapters[k].id))
                                        {
                                            
                                            lastHistory ='<div class="VideoList__Videos"><a href="/DetailVideo/'+CategoryFilm[i].films[j].chapters[k].id+'" class="Videos__video"><div class="Videos__video__content"><div class="video__content__img"><img src="'+CategoryFilm[i].films[j].chapters[k].chapterImage+'" alt=""></div><div class="video__content__video"><video src="'+CategoryFilm[i].films[j].chapters[k].video+'" autoplay muted="true"></video></div><div class="video__content__des"><div class="content__des__body"><div class="des__body__listIcon"><div class="listIcon__icon"><i class="fa-solid fa-play"></i></div></div><div class="des__body__title"><p>'+CategoryFilm[i].films[j].chapters[k].chapterName+'</p><p>Thể loại: <span>'+CategoryFilm[i].categoryName+'</span></p></div></div></div></div></a></div>';
                                        
                                        }
                                    }
                                    
                                }
                                CategoryFilmDetailHtml+=lastHistory;
                                
                            }
                           
                        }
                        catch{
                            
                        }
                    }
                    CategoryFilmDetailHtml+='<span class="next"><i class="fa-solid fa-angle-right"></i></span></div></div>';
                    if(countFilm>0){

                        CategoryFilmHtml+=CategoryFilmDetailHtml;
                    }
                    else{
                        demCategory--;
                    }
                }
            }
            CategoryFilmElement.innerHTML = CategoryFilmHtml;
            prevs=document.querySelectorAll('.ToeicVideos__VideoList .prev');
            nexts=document.querySelectorAll('.ToeicVideos__VideoList .next');
            
            for(var i=0;i<demCategory+1;i++){
                

                checkPrevAndNext(i);
            }
        }
        else if(xhttp.status==403){
            localStorage.removeItem("Token");
            window.location="/Login"
        }
    }         
    //khai báo phương thức và đường dẫn để request
    xhttp.open("GET", "/ApiV1/CategoryAllFim",false);
    //định dạng gửi đi787
    xhttp.setRequestHeader("Content-type","application/json")
    token = localStorage.getItem("Token");
    authorization ='Bearer '+token
    xhttp.setRequestHeader("Authorization",authorization);
    xhttp.send();
}    

function GetHotChapter(){
    const xhttp = new XMLHttpRequest();
    xhttp.onload = function() 
    {
        var ChapterHotJson=xhttp.responseText
        var ChapterHot= JSON.parse(ChapterHotJson)[0]
        if(xhttp.status==200)
        {
            
           
            var ChapterHotElement = document.getElementById('ChapterHot');
            var ChapterHotHtml='';
            ChapterHotHtml+='<div class="movieHots__movie__content" id="movie__hotFirst"><div class="movie__content__img"><img src="'+ChapterHot.chapterImage +'" alt=""></div><div class="movie__content__video"><video src="'+ChapterHot.trailerChapter+'" autoplay="true" muted="true"></video></div></div><div class="movieHots__movie__body"><div class="movie__body__aboutMovie"><div class="aboutMovie__imgName"><img src="'+ChapterHot.film.bannerFilmName+'" alt=""></div><div class="aboutMovie__des"><h1>Xem ngay mùa 2</h1><p>'+ChapterHot.chapterDescription+'</p></div><div class="aboutMovie__action"><a href="/DetailVideo/'+ChapterHot.id+'" class="aboutMovie__action__play"><button><i class="fa-solid fa-play"></i> Play</button></a><button class="aboutMovie__action__infor"><i class="fa-solid fa-circle-info"></i> Thông tin khác</button></div></div></div>';
            ChapterHotElement.innerHTML = ChapterHotHtml;
            
        }
        else if(xhttp.status==403){
            localStorage.removeItem("Token");
            window.location="/Login"
        }
    }         
    //khai báo phương thức và đường dẫn để request
    xhttp.open("GET", "/ApiV1/ChapterHot",false);
    //định dạng gửi đi787
    xhttp.setRequestHeader("Content-type","application/json")
    token = localStorage.getItem("Token");
    authorization ='Bearer '+token
    xhttp.setRequestHeader("Authorization",authorization);
    xhttp.send();
}    
function Search(){
    if(event.keyCode==13){
        const xhttp = new XMLHttpRequest();
        var searchFilmValue =document.getElementById('searchFilm').value;
        xhttp.onload = function() 
        {
            var listFilmJson=xhttp.responseText
            var listFilm= JSON.parse(listFilmJson)
            if(xhttp.status==200)
            {
               
                var ChapterHotElement = document.getElementById('listFilmSearch');
                var ChapterHotHtml='';
                ChapterHotHtml+='<div class="ToiecVideos__ToiecName"><h1>SearchFilm</h1></div><div class="ToeicVideos__VideoList"><span class="prev"><i class="fa-solid fa-angle-left"></i></span>'
                for (var i=0;i<listFilm.length;i++){

                    ChapterHotHtml+='<div class="VideoList__Videos"><a href="" class="Videos__video"><div class="Videos__video__content"><div class="video__content__img"><img src="'+listFilm[i].chapters[0].chapterImage+'" alt=""></div><div class="video__content__video"><video src="'+listFilm[i].chapters[0].trailerChapter+'" autoplay muted="true"></video></div><div class="video__content__des"><div class="content__des__body"><div class="des__body__listIcon"><div class="listIcon__icon"><i class="fa-solid fa-play"></i></div></div><div class="des__body__title"><p>'+listFilm[i].chapters[0].chapterName+'</p><p>Thể loại: <span>'+listFilm[i].chapters[0].chapterDescription+'</span></p></div></div></div></div></a></div>';
                }
                ChapterHotHtml+='<span class="next"><i class="fa-solid fa-angle-right"></i></span></div>';
                ChapterHotElement.innerHTML = ChapterHotHtml;
                var detailVideos = document.querySelectorAll(".Videos__video:not(.Video__final):not(.Video__first)");
                sumOfArray(detailVideos);
                check[0]=0;
                prevx=document.querySelector('.ToeicVideos__VideoList .prev');
                nextx=document.querySelector('.ToeicVideos__VideoList .next');
                if(check[0]==0)
                {
                    prevx.style.display='none';
                    
                }
                else if(check[0]==5){
                    
                    nextx.style.display='none';
                }
                else{
                    prevx.style.display='flex';
                    nextx.style.display='flex';
                }
                
                prevx.addEventListener('click',()=>{
        
                    var videoAll=nextx.parentElement.querySelectorAll('.VideoList__Videos');
                    check[0] -=1;
                
                    videoAll.forEach((element
                    )=>{
                        element.style.transform='translateX(-'+500*check[0]+'%)';
                        
                        if(0+1==(check[0]+1)*5+6){
                            element.querySelector('.Videos__video').classList.remove("Video__final");
                            element.querySelector('.Videos__video').addEventListener('mouseenter',mouseenterDetailVideo);
                            element.querySelector('.Videos__video').addEventListener('mouseleave',mouseleaveDetailVideo);
                        }
                        else if(0+1==check[0]*5+6)
                        {
                            element.querySelector('.Videos__video').classList.add("Video__final");
                            element.querySelector('.Videos__video').removeEventListener('mouseenter',mouseenterDetailVideo);
                            element.querySelector('.Videos__video').removeEventListener('mouseleave',mouseleaveDetailVideo);
                        }
                        else if(0+1==check[0]*5)
                        {
                            element.querySelector('.Videos__video').classList.add("Video__first")
                            element.querySelector('.Videos__video').removeEventListener('mouseenter',mouseenterDetailVideo);
                            element.querySelector('.Videos__video').removeEventListener('mouseleave',mouseleaveDetailVideo);
                        }
                        else if(0+1==(check[0]+1)*5)
                        {
                            element.querySelector('.Videos__video').classList.remove("Video__first");
                            element.querySelector('.Videos__video').addEventListener('mouseenter',mouseenterDetailVideo);
                            element.querySelector('.Videos__video').addEventListener('mouseleave',mouseleaveDetailVideo);
                        }
                        if(check[0]==0)
                        {
                            if(0+1==check[0]*5)
                            {
                                element.querySelector('.Videos__video').classList.remove("Video__first");
                                element.querySelector('.Videos__video').addEventListener('mouseenter',mouseenterDetailVideo);
                                element.querySelector('.Videos__video').addEventListener('mouseleave',mouseleaveDetailVideo);
                            }
                        }
                    });
                    
                    
                    if(check[0]==0)
                {
                    prevx.style.display='none';
                    
                }
                else if(check[0]==5){
                    
                    nextx.style.display='none';
                }
                else{
                    prevx.style.display='flex';
                    nextx.style.display='flex';
                }
                    
                   
                    
                }) 
                
                
                nextx.addEventListener('click',() => {
        
                    var videoAll=nextx.parentElement.querySelectorAll('.VideoList__Videos');
                    
                    check[0] +=1;
                    
                    videoAll.forEach((element)=>{
        
                        element.style.transform='translateX(-'+500*check[0]+'%)';
        
                        if(0+1==(check[0]-1)*5+6){
                            element.querySelector('.Videos__video').classList.remove("Video__final");
                            element.querySelector('.Videos__video').addEventListener('mouseenter',mouseenterDetailVideo);
                            element.querySelector('.Videos__video').addEventListener('mouseleave',mouseleaveDetailVideo);
                        }
                        else if(0+1==check[0]*5+6)
                        {
                            element.querySelector('.Videos__video').classList.add("Video__final");
                            element.querySelector('.Videos__video').removeEventListener('mouseenter',mouseenterDetailVideo);
                            element.querySelector('.Videos__video').removeEventListener('mouseleave',mouseleaveDetailVideo);
                        }
                        else if(0+1==check[0]*5)
                        {
                            element.querySelector('.Videos__video').classList.add("Video__first");
                            element.querySelector('.Videos__video').removeEventListener('mouseenter',mouseenterDetailVideo);
                            element.querySelector('.Videos__video').removeEventListener('mouseleave',mouseleaveDetailVideo);
                        }
                        else if(0+1==(check[0]-1)*5)
                        {
                            element.querySelector('.Videos__video').classList.remove("Video__first");
                            element.querySelector('.Videos__video').addEventListener('mouseenter',mouseenterDetailVideo);
                            element.querySelector('.Videos__video').addEventListener('mouseleave',mouseleaveDetailVideo);
                        }
                        if(check[0]==4)
                        {
                            if(0+1==check[0]*5+6)
                            {
                                element.querySelector('.Videos__video').classList.remove("Video__final");
                                element.querySelector('.Videos__video').addEventListener('mouseenter',mouseenterDetailVideo);
                                element.querySelector('.Videos__video').addEventListener('mouseleave',mouseleaveDetailVideo);
                            }
                        }
        
                    });
                    if(check[0]==0)
                {
                    prevx.style.display='none';
                    
                }
                else if(check[0]==5){
                    
                    nextx.style.display='none';
                }
                else{
                    prevx.style.display='flex';
                    nextx.style.display='flex';
                }
                
                })
                
                
            }
            else if(xhttp.status==403){
                localStorage.removeItem("Token");
                window.location="/Login"
            }
        }         
        //khai báo phương thức và đường dẫn để request
        xhttp.open("GET", "/ApiV1/FindFilm/"+searchFilmValue,false);
        //định dạng gửi đi787
        xhttp.setRequestHeader("Content-type","application/json")
        token = localStorage.getItem("Token");
        authorization ='Bearer '+token
        xhttp.setRequestHeader("Authorization",authorization);
        xhttp.send();
    }
}
const header = document.querySelector('#header');

window.addEventListener('load', function() {
    var video =document.querySelector("#movie__hotFirst .movie__content__video video");
    
  });
let timeoutId;
const movieHot =document.querySelector(".container__movieHots__movie");
var video =document.querySelector("#movie__hotFirst .movie__content__video video");
video.pause();

var mute =document.querySelector("#header .header__mute .header_mute__icon");

mute.addEventListener("click",()=>{
    if(video.muted)
    {
        video.muted=false;
        mute.innerHTML="<i class='icon-volume'></i>";
        
    }
    else{
        video.muted=true;
        mute.innerHTML="<i class='icon-volume-off-1'></i>";
    }
});

let g=0;
video.addEventListener("timeupdate",()=>{
   
    if(document.querySelector("#movie__hotFirst .movie__content__video video").currentTime>0){
        
    if(document.querySelector("#movie__hotFirst .movie__content__video video").currentTime>=2)
    {
        document.querySelector(".movie__body__aboutMovie .aboutMovie__des").classList.add("maxheight");
        document.querySelector(".movie__body__aboutMovie .aboutMovie__imgName ").style.width="60%";
    }
    else{
        document.querySelector(".movie__body__aboutMovie .aboutMovie__des").classList.remove("maxheight");
        document.querySelector(".movie__body__aboutMovie .aboutMovie__imgName ").style.width="100%";
    }
    if(document.querySelector("#movie__hotFirst .movie__content__video video").ended)
    {
        document.querySelector(".movie__body__aboutMovie .aboutMovie__des").classList.remove("maxheight");
        document.querySelector(".movie__body__aboutMovie .aboutMovie__imgName ").style.width="100%";
        document.querySelector("#movie__hotFirst .movie__content__video").style.opacity=0;
        document.querySelector("#movie__hotFirst .movie__content__img").style.opacity = 1;
        
        
        setTimeout(function (){
            
            document.querySelector("#movie__hotFirst .movie__content__video").style.display="none";
            
            document.querySelector("#movie__hotFirst .movie__content__img").style.display="block";
            g=0;

            document.querySelector("#movie__hotFirst .movie__content__video video").currentTime=0;
            document.querySelector("#movie__hotFirst .movie__content__video video").pause();
         
            
        },1000)
    }}
}
)
movieHot.addEventListener("mouseenter", () => {
   
timeoutId = setTimeout(() => {
    
    if(g!==0){
    document.querySelector("#movie__hotFirst .movie__content__video").style.transition="opacity 1s linear";
    document.querySelector("#movie__hotFirst .movie__content__img").style.transition="opacity 1s linear";
    document.querySelector("#movie__hotFirst .movie__content__video").style.opacity=1;
    document.querySelector("#movie__hotFirst .movie__content__img").style.opacity = 0;
    
    document.querySelector("#movie__hotFirst .movie__content__video video").play();
    
    setTimeout(function (){
       
        document.querySelector("#movie__hotFirst .movie__content__video").style.display="block";

        document.querySelector("#movie__hotFirst .movie__content__img").style.display="none";
        
    },1000);}
}, 1000);

g++;
});

movieHot.addEventListener("mouseleave", () => {
    clearTimeout(timeoutId);
    document.querySelector("#movie__hotFirst .movie__content__video").style.opacity=0;
    document.querySelector("#movie__hotFirst .movie__content__img").style.opacity = 1;
    document.querySelector("#movie__hotFirst .movie__content__video video").pause();
    setTimeout(function (){
        document.querySelector("#movie__hotFirst .movie__content__video").style.display="none";
        document.querySelector("#movie__hotFirst .movie__content__video video").pause();
        document.querySelector("#movie__hotFirst .movie__content__img").style.display="block";
    },1000)
});



//cuộn màn hình
window.addEventListener('scroll', () => {
if (window.scrollY > 0) {
    header.classList.add('scroll');
} else {
    header.classList.remove('scroll');
}
});

//thanh srearch
var search=document.querySelector(".header__userInfor__search input");
search.addEventListener("focus",function(){
    document.querySelector(".header__userInfor__search i:first-of-type ").classList.add("focus");
    
});
search.addEventListener("blur",function(){
    document.querySelector(".header__userInfor__search i:first-of-type ").classList.remove("focus");
});
function searchApear(){
    document.querySelector(".header__userInfor__search input").focus();
}


var detailoutId;
//detailVideo
var detailVideos = document.querySelectorAll(".Videos__video:not(.Video__final):not(.Video__first)");
sumOfArray(detailVideos);
function mouseenterDetailVideo(e)
{
    var element = e.target || e.srcElement;
   
    {
        detailoutId = setTimeout(() => {
            
            element.querySelector(".video__content__img").style.opacity=0;
            element.querySelector(".video__content__video").style.opacity=1;
            element.querySelector(".video__content__video").style.height="55%"; 
            element.querySelector(".video__content__img").style.height="55%"; 
            element.querySelector(".Videos__video__content").classList.add("videoScale");
            element.parentElement.style.zIndex="100000";
            element.querySelector(".video__content__img").style.display="none";
            element.querySelector(".video__content__video").style.display="block";
            var videodetail =element.querySelector('video');
            
            videodetail.muted=false;
            videodetail.play();
            videodetail.addEventListener('timeupdate',() => 
            {
                if(videodetail.ended)
                {
                    videodetail.muted=true;
                    videodetail.currentTime=0;
                    videodetail.pause();
                }
            })
        },500);
    }
}
function mouseleaveDetailVideo(e){
    var element = e.target || e.srcElement;
    {
        clearTimeout(detailoutId);
        
        element.querySelector(".video__content__img").style.opacity=1;
        element.querySelector(".video__content__video").style.opacity=0;
        element.querySelector(".video__content__img").style.height="100%"; 
        element.querySelector(".video__content__video").style.height="100%";
        element.querySelector(".Videos__video__content").classList.remove("videoScale");
        
        setTimeout(()=>{

            element.querySelector(".video__content__img").style.display="block";
            element.querySelector(".video__content__video").style.display="none";
            var videodetail =element.querySelector('video');
            videodetail.pause();
            element.parentElement.style.zIndex="100";
        },500);
    }
}
function sumOfArray(numbers)
{
    numbers.forEach(
    function sumElement(element)
    {
        element.addEventListener("mouseenter",mouseenterDetailVideo)
        element.addEventListener("mouseleave", mouseleaveDetailVideo);
    });
    
}
  

//prev and next

eventforclickprev();
eventforclicknext();
function checkPrevAndNext(index){

    try{

        if(check[index]==0)
        {
            prevs[index].style.display='none';
            
        }
        else if(check[index]==5){
            
            nexts[index].style.display='none';
        }
        else{
            prevs[index].style.display='flex';
            nexts[index].style.display='flex';
        }
    }
    catch{

    }

}
function eventforclickprev(){
    prevs.forEach(function(prev, index, prevs) {
    // code to be executed for each element
    prev.addEventListener('click',()=>{
        
        var videoAll=nexts[index].parentElement.querySelectorAll('.VideoList__Videos');
        check[index] -=1;
    
        videoAll.forEach((element
        )=>{
            element.style.transform='translateX(-'+500*check[index]+'%)';
            
            if(index+1==(check[index]+1)*5+6){
                element.querySelector('.Videos__video').classList.remove("Video__final");
                element.querySelector('.Videos__video').addEventListener('mouseenter',mouseenterDetailVideo);
                element.querySelector('.Videos__video').addEventListener('mouseleave',mouseleaveDetailVideo);
            }
            else if(index+1==check[index]*5+6)
            {
                element.querySelector('.Videos__video').classList.add("Video__final");
                element.querySelector('.Videos__video').removeEventListener('mouseenter',mouseenterDetailVideo);
                element.querySelector('.Videos__video').removeEventListener('mouseleave',mouseleaveDetailVideo);
            }
            else if(index+1==check[index]*5)
            {
                element.querySelector('.Videos__video').classList.add("Video__first")
                element.querySelector('.Videos__video').removeEventListener('mouseenter',mouseenterDetailVideo);
                element.querySelector('.Videos__video').removeEventListener('mouseleave',mouseleaveDetailVideo);
            }
            else if(index+1==(check[index]+1)*5)
            {
                element.querySelector('.Videos__video').classList.remove("Video__first");
                element.querySelector('.Videos__video').addEventListener('mouseenter',mouseenterDetailVideo);
                element.querySelector('.Videos__video').addEventListener('mouseleave',mouseleaveDetailVideo);
            }
            if(check[index]==0)
            {
                if(index+1==check[index]*5)
                {
                    element.querySelector('.Videos__video').classList.remove("Video__first");
                    element.querySelector('.Videos__video').addEventListener('mouseenter',mouseenterDetailVideo);
                    element.querySelector('.Videos__video').addEventListener('mouseleave',mouseleaveDetailVideo);
                }
            }
        });
        
        
        checkPrevAndNext(index);
        
       
        
    }) 
    });
}
function eventforclicknext(){
    nexts.forEach(function(next, index, nexts) {

        next.addEventListener('click',(element) => {
        
            var videoAll=next.parentElement.querySelectorAll('.VideoList__Videos');
            
            check[index] +=1;

            videoAll.forEach((element)=>{

                element.style.transform='translateX(-'+500*check[index]+'%)';

                if(index+1==(check[index]-1)*5+6){
                    element.querySelector('.Videos__video').classList.remove("Video__final");
                    element.querySelector('.Videos__video').addEventListener('mouseenter',mouseenterDetailVideo);
                    element.querySelector('.Videos__video').addEventListener('mouseleave',mouseleaveDetailVideo);
                }
                else if(index+1==check[index]*5+6)
                {
                    element.querySelector('.Videos__video').classList.add("Video__final");
                    element.querySelector('.Videos__video').removeEventListener('mouseenter',mouseenterDetailVideo);
                    element.querySelector('.Videos__video').removeEventListener('mouseleave',mouseleaveDetailVideo);
                }
                else if(index+1==check[index]*5)
                {
                    element.querySelector('.Videos__video').classList.add("Video__first");
                    element.querySelector('.Videos__video').removeEventListener('mouseenter',mouseenterDetailVideo);
                    element.querySelector('.Videos__video').removeEventListener('mouseleave',mouseleaveDetailVideo);
                }
                else if(index+1==(check[index]-1)*5)
                {
                    element.querySelector('.Videos__video').classList.remove("Video__first");
                    element.querySelector('.Videos__video').addEventListener('mouseenter',mouseenterDetailVideo);
                    element.querySelector('.Videos__video').addEventListener('mouseleave',mouseleaveDetailVideo);
                }
                if(check[index]==4)
                {
                    if(index+1==check[index]*5+6)
                    {
                        element.querySelector('.Videos__video').classList.remove("Video__final");
                        element.querySelector('.Videos__video').addEventListener('mouseenter',mouseenterDetailVideo);
                        element.querySelector('.Videos__video').addEventListener('mouseleave',mouseleaveDetailVideo);
                    }
                }

            });
            checkPrevAndNext(index);
        
        }) 
    });
}