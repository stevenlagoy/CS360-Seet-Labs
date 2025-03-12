async function Java_JSOutputStream_jsWrite(lib /*: CJ3Library*/,self /*java object???*/, b /*number, probably*/)
{
    let out = document.getElementById("output").innerHTML+=String.fromCharCode(b);
}

let initialized = false;

(async function ()
{
    await cheerpjInit( {natives: { Java_JSOutputStream_jsWrite }} );
    await cheerpjRunMain(
        "Main",
        "/app/ClearFiles.jar",
    );
    initialized = true;
})();



async function compile()
{
    if(!initialized)
    {
        alert("Not yet initialized, please wait");
        return;
    }

     document.getElementById("output").innerHTML = "";

    cheerpOSAddStringFile("/str/Lab.java", document.getElementById("input").value);

    console.log("Attempting to run the compiler")
    await cheerpjRunMain(
        "com.sun.tools.javac.Main",
        "/app/tools.jar",
        // args
        "-d",
        "/files/",
        "/str/Lab.java",
        "/app/LabLauncher.java",
        "/app/JSOutputStream.java"
    );

    console.log("Attempting to make a jar")
    await cheerpjRunMain(
        "sun.tools.jar.Main",
        "/app/tools.jar",
        // args
        "-cf",   "/files/Lab.jar",
        "-C", "/files",
        "Lab.class",
        "LabLauncher.class",
        "JSOutputStream.class"
    );

    /*await cheerpjRunMain(
        "Main",
        "/app/ListFiles.jar",
    );*/

    console.log("Attempting to run the program")
    await cheerpjRunMain(
        "LabLauncher",
        "/files/Lab.jar",
    );

}

