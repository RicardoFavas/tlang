"# tlang" 

Programming language with based on python with nive async capabilities

var foo = () ->
	var i = 0
	while i != 10000 do 
		i = i + 1
	return i

var r = async foo()
var s = async foo()

if r+s != 2*10000 do
	throw 'it should be 20000'
