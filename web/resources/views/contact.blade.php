@extends('layout.app') @section('content')
    <section class="contact-page">
        <div class="contact-page-container">
            <div class="top-contact">
                <div class="content">
                    <h1 class="contact-title">Contactez-nous<br>Rejoignez la communauté</h1>
                    <div class="input-container">
                        <p class="contact_desc">
                            Vous aimez notre projet ? Vous souhaitez y contribuer ?
                            Rejoignez nous et apportez votre savoir faire où que vous soyez dans le monde !

                        </p>
                    </div>
                </div>
            </div>
            <div class="contact_wrapper">
                <div class="contact_form">
                    <form action="">
                        <div class="half left cf">
                            <div class="input-shadow"></div>
                            <input type="text" class="input-name" placeholder="Votre nom">
                            <input type="email" class="input-email" placeholder="Votre email">
                        </div>
                        <div class="half right cf">
                            <div class="input-shadow input-shadow--area"></div>
                            <textarea name="message" type="text" id="input-message" placeholder="Votre message"></textarea>
                        </div>
                        <input type="submit" value="Envoyer" class="input-submit">
                    </form>
                </div>
                <div class="contact_info">
                    <h3 class="contact_info-title">
                        Informations
                    </h3>
                    <p class="contact_info-desc">
                       Notre  communauté est ouverte à toute personne intéressée à collaborer et contribuer au projet echOpen. N’hésitez pas à nous contacter pour avoir plus d’informations.
                    </p>
                    <div class="contact_info-sub">
                        <p class="info_title">Email</p>
                        <p>contact@echopen.org</p>
                    </div>
                    <div class="contact_info-sub">
                        <p class="info_title">GitHub</p>
                        <p>https://github.com/echopen</p>
                    </div>

                </div>

            </div>

        </div>
        <div class="contact_map-wrapper">
            <div class="contact_map">
            </div>
        </div>
    </section>
@endsection