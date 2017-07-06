@extends('layout.app')

@section('content')
    <section class="product-page">
        <div class="s__pp__header ">
            <div class="s__pp__header__mockup"></div>
            <div class="echopen-text">
                <p>L’échographe portable à la portée de
tout le monde.</p>
            </div>
            <div class="echopen-logo"></div>
            <div class="echopen-bubbles"></div>
        </div>
        <div class="s__pp__content">
            <div class="s__pp__content__article1">
                <div class="s__pp__content__article1__img">
                    <img src="/img/product/article__img1.png" alt="" class="s__pp__content__article__img--img">
                </div>
                <div class="s__pp__content__article1__texts">
                    <p class="s__pp__content__article1__texts--title">Polyvalent, rapide
et optimal</p>
                    <p class="s__pp__content__article1__texts--desc">L’éco-stéthoscope optimal, par echOpen.
Facile à utiliser et toujours à vos côtés.
Il est suffisamment petit pour être transporté dans une poche, si bien que les cardiologues peuvent s’en servir rapidement pour évaluer l’anatomie et la fonction cardiaque.
L’outil fonctionne hors réseaux ce qui vous permet de l’utiliser n’importe où. Un bref coup d’oeil peut en effet apporter des réponses immédiates à des problèmes cardio-vasculaires ciblés, et accélérer le suivi, pour une plus grande efficacité dans l’organisation des tâches et dans la gestion des patients. 
</p>
                </div>
            </div>
            <div class="s__pp__content__article2">
                <div class="s__pp__content__article2__img">
                    <img src="/img/product/article__img2.png" alt="" class="s__pp__content__article__img--img">
                </div>
                <div class="s__pp__content__article2__texts">
                    <p class="s__pp__content__article2__texts--title">Usage simple et universel</p>
                    <p class="s__pp__content__article2__texts--desc">Applications cliniques possibles : Cardiaque, Abdominal, Rénal, Obstétrique et gynécologique, Urologique, la Pédiatrie.</p>
                    <p class="s__pp__content__article2__texts--desc">Plus simple encore, accessible à tous, Monsieur ou Madame tout le monde,  ayant envie d’admirer de plus prêt votre mystérieux corps de l’intérieur.</p>
                </div>
            </div>
        </div>
        <div class="s__pp__great">
            <div class="s__product__title">
                <p class="s__product__title--intro">Une communauté</p>
                <p class="s__product__title--big">Nos points <span> forts</span></p>
            </div>
            <ul class="s__pp__great__collection">
                <li class="s__pp__great__collection__item">
                    <p class="s__pp__great__collection__item--title">Communauté médicale</p>
                    <p class="s__pp__great__collection__item--desc">Les praticiens et professionnels de la santé ont la possibilité de partager leurs clichés sur les réseaux afin d’échanger sur leur contenu médical. Cela anime la communauté, aide aux diagnostics et montre aux utilisateurs comment diagnostiquer un cas en particulier.</p>
                </li>
                <li class="s__pp__great__collection__item">
                    <p class="s__pp__great__collection__item--title">Communauté open-source</p>
                    <p class="s__pp__great__collection__item--desc">EchOpen repose sur une communauté composée d’une grande variété de corps de métiers, tous adepte de l’open source, qui oeuvrent à la réalisation et la mise en place du projet. Tous bénévoles, ils s’entraident pour atteindre leurs objectifs.</p>
                </li>
                <li class="s__pp__great__collection__item">
                    <p class="s__pp__great__collection__item--title">Formation en 2 jours</p>
                    <p class="s__pp__great__collection__item--desc">Nous avons révolutionné la façon de se former. 
Cette dernière se résume à seulement 48 heures d’apprentissage intensif, au lieu d’un cursus  classique de deux  ans dans le domaine de l’échographie.</p>
                </li>
                <li class="s__pp__great__collection__item">
                    <p class="s__pp__great__collection__item--title">e-learning</p>
                    <p class="s__pp__great__collection__item--desc">Pour former en simultané un grand nombre d’utilisateurs à l’utilisation de l’echo-stéthoscope, la mise en place d’une plateforme d’e-learning est primordiale pour garantir une qualité de formation et donc une qualité d’analyse médicale optimale.</p>
                </li>
            </ul>
        </div>
        <div class="s__pp__after">
            <div class="s__product__title">
                <p class="s__product__title--intro">Et apres</p>
                <p class="s__product__title--big">Machine <span>Learning</span></p>
            </div>
            <p class="s__pp__after__text">Grâce à l’apport de la communauté des helpers, l’intelligence artificielle développée  par nos ingénieurs, permettra à la sonde echOpen d’analyser et diagnostiquer les datas prélevées.</p>
            <div class="s__pp__after__cta">
                <button class="s__pp__after__cta--button">Soutenir Echopen</button>
            </div>
        </div>
    </section>
@endsection
