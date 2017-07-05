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
    

</head>
<body>

<header>
    <ul class="links">
        <li><a href="#">Home</a></li>
        <li><a href="#">Product</a></li>
        <li><a href="#">Faq</a></li>
        <li><a href="#">Contact</a></li>
    </ul>
    <div class="logo"></div>
    <ul class="right-cta">
        <li class="join-us">Join us</li>
    </ul>
</header>

<div class="container">
    @yield('content')
</div>

<footer>
    
</footer>

<script
  src="https://code.jquery.com/jquery-3.2.1.min.js"
  integrity="sha256-hwg4gsxgFZhOsEEamdOYGBf13FyQuiTwlAQgxVSNgt4="
  crossorigin="anonymous"></script>

<script type="text/javascript" src="//cdn.jsdelivr.net/jquery.slick/1.6.0/slick.min.js"></script>

</body>
</html>