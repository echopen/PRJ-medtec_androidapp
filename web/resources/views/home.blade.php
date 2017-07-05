@extends('layout.app')

@section('content')
    <section class="welcome">
        @include('components.home_presentation')
    </section>
    
    <section class="base_section base_section-faq">
       @include('components.home_faq')
    </section>
    
     
    <section class="base_section base_section-community">
       @include('components.home_community')
    </section>

     <section class="base_section base_section-howItWorks">
       @include('components.home_howItWorks')
    </section>
    
@endsection
