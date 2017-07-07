<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script src="/js/libs/ScrollMagic.min.js"></script>
<script src="/js/libs/TweenMax.js"></script>
<script src="/js/libs/animation.gsap.min.js"></script>
<script src="/js/libs/debug.addIndicators.min.js"></script>

<script src="/js/libs/TimelineMax.js"></script>

<script>

// When the DOM is ready
$(function() {
  
  var lessThan1024 = function(){
       windowWidth = window.innerWidth;
       return windowWidth < 1024;
   }
    var isMobile = lessThan1024();

    $(window).resize(function() {
        isMobile = lessThan1024();
    })

$('.js-link').on('click', function(e){
    e.preventDefault();
    const top = $(this).data('section');
        $('html, body').animate({
            scrollTop: top
        }, 400);
})

var scrollMagicController = new ScrollMagic.Controller();

 // tweens

  var tweenTop = TweenMax.from('#screenTop', 0.5, {
    backgroundColor: 'rgb(255, 39, 46)',
    //opacity: 0,
    top: "-100%",
  });

  var tweenLeft = TweenMax.from('#screenLeft', 0.5, {
    backgroundColor: 'rgb(255, 39, 46)',
    opacity: 0,
    //left: "-100%",
  });

  var tweenRight = TweenMax.from('#screenRight', 0.5, {
    backgroundColor: 'rgb(255, 39, 46)',
    opacity: 0,
    //right: "-100%",
  });
  var tweenBottom = TweenMax.from('#screenBottom', 0.5, {
    backgroundColor: 'rgb(255, 39, 46)',
    //opacity: 0,
    bottom: "-100%",
  });
  

// scenes

  var sceneTop = new ScrollMagic.Scene({
      triggerElement: '#scene',
      offset: 350
  })
 
  .setTween(tweenTop)
  .setClassToggle("#list1", 'active')
  .addTo(scrollMagicController);
  //sceneTop.addIndicators();
  
  var sceneGlobal = new ScrollMagic.Scene({
      triggerElement: '#scene',
      offset: 525,
      duration: 2175
  })
  
   sceneGlobal.addTo(scrollMagicController);

    var needToRemoveScrollPin = function(){
        if (!isMobile){
            sceneGlobal.setPin('.scrollVertical');
        } else {
            sceneGlobal.removePin(true);
        };
    }

    sceneGlobal.on("start", function (event) {
        needToRemoveScrollPin();
    });
    sceneGlobal.on("leave", function (event) {
        needToRemoveScrollPin();
    });



  var sceneRight = new ScrollMagic.Scene({
      triggerElement: '#scene',
      offset: 1000
  })
  .setTween(tweenRight)
  .setClassToggle("#list2", 'active')
  .addTo(scrollMagicController)
  //sceneRight.addIndicators();

  var sceneLeft = new ScrollMagic.Scene({
      triggerElement: '#scene',
      offset: 1500
  })
  .setTween(tweenLeft)
  .setClassToggle("#list3", 'active')
  .addTo(scrollMagicController);
  //sceneLeft.addIndicators();

  var sceneBottom = new ScrollMagic.Scene({
      triggerElement: '#scene',
      offset: 2000
  })
  .setTween(tweenBottom)
  .setClassToggle("#list4", 'active')
  .addTo(scrollMagicController);
  //sceneBottom.addIndicators();

  
});
    
</script>

<div class="s__hiw" id="scene">
    <div class="scrollVertical">
        <div class="s__hiw__title">
            <p class="s__hiw__title__category">la sonde</p>
            <h4 class="s__hiw__title__title">Fonctionnement et atouts</h4>
        </div>
        <div class="s__hiw__content">
            <div class="s__hiw__content__anim">
                <div class="s__hiw__content__anim__mockupLandmark">
                    <div class="s__hiw__content__anim__mockupLandmark__collection">
                        <img class="s__hiw__content__anim__mockupLandmark__collection--mockup" src="/img/nexus_mockup.png" alt="">
                        <div class="s__hiw__content__anim__mockupLandmark__collection--mask">
                            <img src="/img/mockup__content/base.png" alt="" id="screenBasic" class="screenBasic">
                            <img src="/img/mockup__content/quickmode.png" alt="" id="screenTop" class="screenTop">
                            <img src="/img/mockup__content/custommode.png" alt="" id="screenLeft" class="screenLeft">
                            <img src="/img/mockup__content/modif.png" alt="" id="screenRight" class="screenRight">
                            <img src="/img/mockup__content/analyze.png" alt="" id="screenBottom" class="screenBottom">
                            
                        </div>
                    </div>
                </div>
            </div>
            <div class="s__hiw__content__text">
                <ul class="s__hiw__content__text__collection">
                    <li class="s__hiw__content__text__collection__item" id="list1">
                        <span></span>
                        <a href="" alt="jump to section 3" class="s__hiw__content__text__collection__item__texts js-link" data-section="3600">
                            <p class="s__hiw__content__text__collection__item__texts--title">Quick mode</p>
                            <p class="s__hiw__content__text__collection__item__texts--desc">Le Quick mode permet aux utilisateurs dans l’urgence d’utiliser l’appareil sans prédéfinir de réglages. Des réglages par défaut seront alors appliqués lors de la prise de vue.</p>
                        </a>
                    </li>
                    <li class="s__hiw__content__text__collection__item" id="list2">
                        <span></span>
                        <a href="" alt="jump to section 3" class="s__hiw__content__text__collection__item__texts js-link"  data-section="4500">
                            <p class="s__hiw__content__text__collection__item__texts--title">Custom mode</p>
                            <p class="s__hiw__content__text__collection__item__texts--desc">Le Custom mode permet à tout utilisateurs de paramétrer la prise d’images avant de commencer (âge, sexe du patient, organe a analyser, grain, etc…).</p>
                        </a>
                    </li>
                    <li class="s__hiw__content__text__collection__item" id="list3">
                        <span></span>
                        <a href="" alt="jump to section 3" class="s__hiw__content__text__collection__item__texts js-link" data-section="5000">
                            <p class="s__hiw__content__text__collection__item__texts--title">Modifications paramètres en temps réel</p>
                            <p class="s__hiw__content__text__collection__item__texts--desc">Lorsque l’utilisateur arrive sur l’écran de prise d’images, il a l’a possibilité de modifier certains paramètre lors de ses prises de vue (le grain par exemple avec la règle sur le côté droit).</p>
                        </a>
                    </li>
                    <li class="s__hiw__content__text__collection__item" id="list4">
                        <span></span>
                        <a href="" alt="jump to section 3" class="s__hiw__content__text__collection__item__texts js-link" data-section="5500">
                            <p class="s__hiw__content__text__collection__item__texts--title">Analyse de l’image</p>
                            <p class="s__hiw__content__text__collection__item__texts--desc">Après avoir pris une ou plusieurs images à l’aide de votre Smartphone. Vous pourrez les retrouver dans la partie dédiée (swipe vers la droite) de l’application pour les analyser et prendre des mesures.</p>
                        </a>
                    </li>
                </ul>
                <div class="s__hiw__content__text__cta">
                    <button class="s__hiw__content__text__cta--button">En savoir plus</button>
                </div>
            </div>
        </div>
    </div>
</div>