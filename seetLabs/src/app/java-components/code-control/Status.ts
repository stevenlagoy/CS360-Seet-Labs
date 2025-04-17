
export class Status
{
  private _buttonStatus:string ="Initializing";
  private _additionalStatus:string = "";
  private _compilationButtonAllowed:boolean = false;
  private _programRunning = false;
  private _class = "";

  get buttonStatus() {return this._buttonStatus;}
  get additionalStatus(){return this._additionalStatus;}
  get compileButtonDisabled(){return !this._compilationButtonAllowed;}
  get class(){return this._class;}

  get programRunning(){return this._programRunning;}
  set programRunning(value:boolean){this._programRunning = value;}

  public setStatus(status:string, isAdditional:boolean, compilationAllowed?:boolean):void
  {
    this._class="";
    if(compilationAllowed == undefined)
    {
      compilationAllowed = isAdditional;
    }

    this._compilationButtonAllowed = compilationAllowed;

    if(isAdditional)
    {
      this._additionalStatus = status;
      this._buttonStatus = "Compile";
      return;
    }

    this._additionalStatus="";
    this._buttonStatus=status;
    
  }

  public setStatusClass(str:string)
  {
    this._class = str;
  }
}