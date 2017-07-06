@extends('layout.app')

@section('content')
    <section class="welcome">
        @include('components.home_presentation')
    </section>
    @include('components.home_nl')
    @include('components.home_history')
    @include('components.home.community')
    <section class="base_section base_section-howItWorks">
        @include('components.home_howItWorks')
    </section>
    @include('components.home_nl')
    <section class="base_section base_section-numbers">
        @include('components.home_numbers')
    </section>
    @include('components.home_faq')
    @include('components.home_community')
    @include('components.home.partners')

@endsection
