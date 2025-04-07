
export class Status
{
  private _buttonStatus:string ="Initializing";
  private _additionalStatus:string = "";
  private _compilationButtonAllowed:boolean = false;
  private _programRunning = false;

  get buttonStatus() {return this._buttonStatus;}
  get additionalStatus(){return this._additionalStatus;}
  get compileButtonDisabled(){return !this._compilationButtonAllowed;}

  get programRunning(){return this._programRunning;}
  set programRunning(value:boolean){this._programRunning = value;}

  public setStatus(status:string, isAdditional:boolean, compilationAllowed?:boolean):void
  {
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
}