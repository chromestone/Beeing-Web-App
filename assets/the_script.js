the_form = null;
window.onload = function() {
	the_form = document.getElementById("the_form");
	var vid_thing = document.getElementById("video");
	var xmlHttp = new XMLHttpRequest();
	xmlHttp.onreadystatechange = function() {
		if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
			vid_thing.src = "https://www.youtube.com/embed/" + xmlHttp.responseText;
		}
	}
	xmlHttp.open("GET", "link", true); // true for asynchronous
	xmlHttp.send(null);
}
var html = ['<div class="column" style="width:15vw;">',
'<div>',
'<label>Hour </label>',
'<input id="_start_hh" type="number" min="0" value="0" form="the_form">',
'</div>',
'<div>',
'<label>Minute </label>',
'<input id="_start_mm" type="number" min="0" max="59" value="0" form="the_form">',
'</div>',
'<div>',
'<label>Second </label>',
'<input id="_start_ss" type="number" min="0" max="59" value="0" form="the_form">',
'</div>',
'</div>',
'<div class="column" style="width:15vw;">',
'<div>',
'<label>Hour </label>',
'<input id="_end_hh" type="number" min="0" value="0" form="the_form">',
'</div>',
'<div>',
'<label>Minute </label>',
'<input id="_end_mm" type="number" min="0" max="59" value="0" form="the_form">',
'</div>',
'<div>',
'<label>Second </label>',
'<input id="_end_ss" type="number" min="0" max="59" value="0" form="the_form">',
'</div>',
'</div>',
'<div class="column" style="width:55vw;">',
'<input id="_desc" type="text" style="width: 54vw;" form="the_form">',
'</div>'].join('');
id_num = 0;
function add_record() {
	var div = document.createElement('div');
	div.setAttribute('class', 'row');
	div.setAttribute('style', 'background-color:white; color:black;');
	div.innerHTML = html;
	document.getElementById('thing').appendChild(div);
	var z;
	z = document.getElementById('_start_hh');
	z.id = 'start_hh' + id_num;
	z.name = z.id;
	z = document.getElementById('_start_mm');
	z.id = 'start_mm' + id_num;
	z.name = z.id;
	z = document.getElementById('_start_ss');
	z.id = 'start_ss' + id_num;
	z.name = z.id;
	z = document.getElementById('_end_hh');
	z.id = 'end_hh' + id_num;
	z.name = z.id;
	z = document.getElementById('_end_mm');
	z.id = 'end_mm' + id_num;
	z.name = z.id;
	z = document.getElementById('_end_ss');
	z.id = 'end_ss' + id_num;
	z.name = z.id;
	z = document.getElementById('_desc');
	z.id = 'desc' + id_num;
	z.name = z.id;
	id_num += 1;
}
function next_page(btn) {

    btn.disabled = true;
	the_form.submit();
}