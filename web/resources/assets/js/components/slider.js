$(document).ready(function(){
    $('.entry_slick-multiple').slick({
      slidesToShow: 3,
      slidesToScroll: 1,
      speed: 400,
      variableWidth: true,
   
  });
    $('.entry_slick-solo').slick({
      slidesToShow: 1,
      slidesToScroll: 1,
      speed: 400,
      dots: true,
      arrows: true,
      variableWidth: true,
      
  });    
});	
 $(window).ready(function(){
     setTimeout(function(){
        $(' .slick-current').css("margin-left","30px");
     },300) //pas le choix pour afficher shadow du premier enfant
     
    $('.base_section-faq .slick_controls .slick_left').click(function(){
        $(".entry_slick-multiple").slick('slickPrev');
    });
    $('.base_section-faq .slick_controls .slick_right').click(function(){
        $(".entry_slick-multiple").slick('slickNext');
    });
     
      $('.base_section-history .slick_controls .slick_left').click(function(){
        $(".entry_slick-solo").slick('slickPrev');
    });
    $('.base_section-history .slick_controls .slick_right').click(function(){
        $(".entry_slick-solo").slick('slickNext');
    });
    
 })