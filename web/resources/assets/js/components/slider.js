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
      variableWidth: true
  });    
});	
