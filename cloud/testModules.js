exports.getMessage = function (request, response) {
  response.success("Good job, buddy");
};

exports.getKeywords = function (request, response) {
	var toLowerCase = function(w) { return w.toLowerCase(); };
	var _ = require("underscore");
  
    var keywords = request.split(' ');
    keywords = _.map(keywords, toLowerCase);
    var stopWords = ["the", "in", "and", "to", "but", "for", "or", "yet", "so"];
     
    Array.prototype.contains = function ( needle ) {
    for (i in this) {
       if (this[i] == needle) return true;
    }
    return false;
    }
     
    keywords = keywords.filter(function (w) {
    return w.match(/[a-zA-Z0-9]+/) && !stopWords.contains(w);
    });
    response.success(keywords);
};