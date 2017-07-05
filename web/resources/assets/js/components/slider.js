console.log("here");

$(document).ready(function(){
    $('.entry_slick-multiple').slick({
      slidesToShow: 2,
      slidesToScroll: 1,
      speed: 400
  });
    $('.entry_slick-solo').slick({
      slidesToShow: 1,
      slidesToScroll: 1,
      speed: 400,
      dots: true
  });    
});	
console.log("hereeee");