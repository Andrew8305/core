require.config({
   shim: {
     "jquery.flot": { exports: "$.plot", deps: ["jquery"] },
     "bootstrap": ["jquery"],
     "bootbox": { exports: "bootbox", deps: ["jquery"] }
   },
   paths : {
      "knockout" : "//cdnjs.cloudflare.com/ajax/libs/knockout/3.2.0/knockout-min",
      "jquery" : "//ajax.googleapis.com/ajax/libs/jquery/2.1.0/jquery.min",
      "bootstrap" : "//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min",
      "bootbox" : "//cdnjs.cloudflare.com/ajax/libs/bootbox.js/4.2.0/bootbox.min",
      "jquery.flot" : "//cdnjs.cloudflare.com/ajax/libs/flot/0.8.2/jquery.flot.min",
      "protocol" : "../../protocol"
   }
});

require(["app", "cluster", "bootstrap", "jquery", "jquery.flot"], function(app, model) {
   setTimeout(function() {
      app.run(model);
   }, 0);
});