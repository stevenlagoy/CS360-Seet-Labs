async function Java_JSOutputStream_jsWrite(lib /*: CJ3Library*/,self /*java object???*/, b /*number, probably*/)
{
    let out = document.getElementById("output").innerHTML+=String.fromCharCode(b);
}



(async function ()
{
    await cheerpjInit( {natives: { Java_JSOutputStream_jsWrite }} );
    let val = await cheerpjRunMain(
        "Main",
        "/app/ClearFiles.jar",
    );
    if(await val !== 0)
    {
        alert("Initialization failed.");
        return;
    }

    document.getElementById("compile").removeAttribute("disabled");
    document.getElementById("status").innerHTML = "Status: Ready";
})();



async function compile()
{
    document.getElementById("compile").setAttribute("disabled", "");

    document.getElementById("output").innerHTML = "";

    cheerpOSAddStringFile("/str/Lab.java", document.getElementById("input").value);

     let retVal = 0;

    document.getElementById("status").innerHTML = "Status: Compiling";
    retVal = await cheerpjRunMain(
        "com.sun.tools.javac.Main",
        "/app/tools.jar",
        // args
        "-d",
        "/files/",
        "/str/Lab.java",
        "/app/LabLauncher.java",
        "/app/JSOutputStream.java"
    );
    if(await retVal !== 0)
    {
        document.getElementById("status").innerHTML = "Status: Compilation Failed";
        return;
    }

    document.getElementById("status").innerHTML = "Status: Making Jar";
    retVal = await cheerpjRunMain(
        "sun.tools.jar.Main",
        "/app/tools.jar",
        // args
        "-cf",   "/files/Lab.jar",
        "-C", "/files",
        "Lab.class",
        "LabLauncher.class",
        "JSOutputStream.class"
    );

    if(await retVal !== 0)
    {
        document.getElementById("status").innerHTML = "Status: Failed to make Jar.";
        return;
    }


    document.getElementById("status").innerHTML = "Status: Running Program";
    retVal = await cheerpjRunMain(
        "LabLauncher",
        "/files/Lab.jar",
    );


    document.getElementById("status").innerHTML = "Status: Returned with code "+await retVal;
    document.getElementById("compile").removeAttribute("disabled");

}

