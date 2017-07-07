<html>
<head>
    <title>Echopen</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="csrf-token" content="{{ csrf_token() }}">
    <link rel="stylesheet" href="{{ mix('/css/app.css') }}">
    <script type="text/javascript" src="{!! mix('/js/app.js') !!}"></script>

    <link rel="stylesheet" type="text/css" href="//cdn.jsdelivr.net/jquery.slick/1.6.0/slick.css"/>
    <!-- Add the slick-theme.css if you want default styling -->
    <link rel="stylesheet" type="text/css" href="//cdn.jsdelivr.net/jquery.slick/1.6.0/slick-theme.css"/>

    <link rel="apple-touch-icon" sizes="57x57" href="/img/icons/apple-icon-57x57.png">
    <link rel="apple-touch-icon" sizes="60x60" href="/img/icons/apple-icon-60x60.png">
    <link rel="apple-touch-icon" sizes="72x72" href="/img/icons/apple-icon-72x72.png">
    <link rel="apple-touch-icon" sizes="76x76" href="/img/icons/apple-icon-76x76.png">
    <link rel="apple-touch-icon" sizes="114x114" href="/img/icons/apple-icon-114x114.png">
    <link rel="apple-touch-icon" sizes="120x120" href="/img/icons/apple-icon-120x120.png">
    <link rel="apple-touch-icon" sizes="144x144" href="/img/icons/apple-icon-144x144.png">
    <link rel="apple-touch-icon" sizes="152x152" href="/img/icons/apple-icon-152x152.png">
    <link rel="apple-touch-icon" sizes="180x180" href="/img/icons/apple-icon-180x180.png">
    <link rel="icon" type="image/png" sizes="192x192"  href="/img/icons/android-icon-192x192.png">
    <link rel="icon" type="image/png" sizes="32x32" href="/img/icons/favicon-32x32.png">
    <link rel="icon" type="image/png" sizes="96x96" href="/img/icons/favicon-96x96.png">
    <link rel="icon" type="image/png" sizes="16x16" href="/img/icons/favicon-16x16.png">
    <link rel="manifest" href="/img/icons/manifest.json">
    <meta name="msapplication-TileColor" content="#ffffff">
    <meta name="msapplication-TileImage" content="/img/icons/ms-icon-144x144.png">
    <meta name="theme-color" content="#ffffff">
</head>
<body>

<header>
    <div class="header">
        <a href="#" class="header__mobileHamburger">
            <img src="/img/menu.png" alt="" class="header__mobileHamburger--img">
        </a>
        <ul class="header__nav">
            <li class="header__nav__item"><a href="/" class="header__nav__item--link">Découvrir le projet</a></li>
        </ul>
        <a href="#" class="header__logo">
            <img src="/img/echopen-gradient.png" alt="" class="header__logo--img">
        </a>
        <div class="header__mobileCta">
            <a href="#" class="header__mobileCta--btn">Connexion</a>
        </div>
        <div class="header__cta">
            <a href="#" class="header__cta--btn">Me connecter</a>
        </div>
    </div>
</header>

<div class="container">
    @yield('content')
</div>

<footer>
    <div class="bottom">
        <div class="bg">
            <div class="logo_footer"><img src="/img/echopen-white.png"></div>
            <ul class="links">
                <li><a href="#">Home</a></li>
                <li><a href="#">Le produit</a></li>
                <li><a href="#">Faq</a></li>
                <li><a href="#">Contact</a></li>
                <li><a href="#">wiki</a></li>
                <li><a href="#">Documentation</a></li>
            </ul>
            <ul class="right-cta">
                <li class="join-us">Rejoignez nous</li>
            </ul>
        </div>
    </div>
</footer>

<script
        src="https://code.jquery.com/jquery-3.2.1.min.js"
        integrity="sha256-hwg4gsxgFZhOsEEamdOYGBf13FyQuiTwlAQgxVSNgt4="
        crossorigin="anonymous"></script>

<script type="text/javascript" src="//cdn.jsdelivr.net/jquery.slick/1.6.0/slick.min.js"></script>

</body>
</html>