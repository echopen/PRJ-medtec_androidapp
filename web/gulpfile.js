var gulp = require('gulp');
var sass = require('gulp-sass');
var minifycss = require('gulp-minify-css')
var sync = require('browser-sync');
var connect = require('gulp-connect-php');
var babel = require('gulp-babel');
var browserify = require('gulp-browserify');

gulp.task('scss', function (){
    gulp.src(['src/scss/app.scss'])
        .pipe(sass())
        .pipe(gulp.dest('dist/css'))
        .pipe(minifycss())
    sync.reload();
});

gulp.task('js', function(){
    gulp.src(['src/js/app.js'])
        .pipe(babel({
            presets: ['es2015']
        }))
        .pipe(browserify({
            insertGlobals : true,
            debug : !gulp.env.production
        }))
        .pipe(gulp.dest('dist/js'))

    setTimeout(function(){
        sync.reload();
    }, 500);

})

gulp.task('sync', ['scss'], function() {
    connect.server({}, function (){
        sync({
            proxy: 'echopen.dev',
            port: 3000//TO EDIT
        });
    });

    gulp.watch("src/scss/**/*.scss", ['scss']);
    gulp.watch("src/js/**/*.js", ['js']);
  gulp.watch("src/components/*.html", function(){
    sync.reload();
  });
});

gulp.task('default', ['sync'], function(){

});