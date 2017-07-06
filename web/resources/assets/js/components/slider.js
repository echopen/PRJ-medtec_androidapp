$(document).ready(function(){
    $('.entry_slick-multiple').slick({
      slidesToShow: 3,
      slidesToScroll: 1,
      speed: 400,
      variableWidth: true
  });
    $('.entry_slick-solo').slick({
      slidesToShow: 1,
      slidesToScroll: 1,
      speed: 400,
      dots: true,
      arrows: true,
      variableWidth: true,
      prevArrow:"<div class='slick_btn_wrap'><button type='button' class='slick-prev pull-left'></button></div>",
      nextArrow:"<div class='slick_btn_wrap slick_btn_wrap-left'><button type='button' class='slick-next pull-right'></button></div>"
  });    
});	
