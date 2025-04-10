import { Component, computed, ElementRef, Signal, ViewChild } from '@angular/core';
import {Extension, EditorState} from "@codemirror/state"
import {
  EditorView, keymap, highlightSpecialChars, drawSelection,
  highlightActiveLine, dropCursor, rectangularSelection,
  crosshairCursor, lineNumbers, highlightActiveLineGutter
} from "@codemirror/view"
import {
  defaultHighlightStyle, syntaxHighlighting, indentOnInput,
  bracketMatching, foldGutter, foldKeymap
} from "@codemirror/language"
import {
  defaultKeymap, history, historyKeymap,
  indentWithTab
} from "@codemirror/commands"
import {
  searchKeymap, highlightSelectionMatches
} from "@codemirror/search"
import {
  autocompletion, completionKeymap, closeBrackets,
  closeBracketsKeymap
} from "@codemirror/autocomplete"
import {lintKeymap} from "@codemirror/lint"
import {java} from "@codemirror/lang-java"




@Component({
  selector: 'app-code-editor',
  imports: [],
  templateUrl: './code-editor.component.html',
  styleUrl: './code-editor.component.css'
})
export class CodeEditorComponent
{

  // code mirror
  @ViewChild('codeMirrorInsert') codeMirrorInsert!:ElementRef;
  codeMirrorView! :EditorView;

  private baseCode:string = `public class Lab\n{\n\tpublic static void main(String[] args)\n\t{\n\t\tSystem.out.println(\"Hello, world! \\nThis is from java!\");
  }\n}`;

  public getCode = () => this.codeMirrorView.state.doc.toString();

  public async setBaseCode(baseFile:string)
  { 
    
    await fetch("base-code/"+baseFile)
      .then(res => res.text())
      .then(code => {
        this.baseCode = code;
      })
 
    this.codeMirrorView.dispatch({changes: {
      from: 0,
      to: this.codeMirrorView.state.doc.length,
      insert: this.baseCode
    }})
  }
  

  public reset()
  {
    if(!confirm("Reset the code editor? You will lose any changes you made."))
    {
      return;
    }

    this.codeMirrorView.dispatch({changes: {
      from: 0,
      to: this.codeMirrorView.state.doc.length,
      insert: this.baseCode
    }})

  }




  ngAfterViewInit()
  {
    
    this.codeMirrorView = new EditorView({
      // starting code. Garbage format, I know, but temporary (hopefully).
      doc: this.baseCode,
      parent: this.codeMirrorInsert.nativeElement,
      extensions: [  // A line number gutter
        lineNumbers(),
        // A gutter with code folding markers
        foldGutter(),
        // Replace non-printable characters with placeholders
        highlightSpecialChars(),
        // The undo history
        history(),
        // Replace native cursor/selection with our own
        drawSelection(),
        // Show a drop cursor when dragging over the editor
        dropCursor(),
        // Allow multiple cursors/selections
        EditorState.allowMultipleSelections.of(true),
        // Re-indent lines when typing specific input
        indentOnInput(),
        // Highlight syntax with a default style
        syntaxHighlighting(defaultHighlightStyle),
        // Highlight matching brackets near cursor
        bracketMatching(),
        // Automatically close brackets
        closeBrackets(),
        // Load the autocompletion system
        autocompletion(),
        // Allow alt-drag to select rectangular regions
        rectangularSelection(),
        // Change the cursor to a crosshair when holding alt
        crosshairCursor(),
        // Style the current line specially
        highlightActiveLine(),
        // Style the gutter for current line specially
        highlightActiveLineGutter(),
        // Highlight text that matches the selected text
        highlightSelectionMatches(),
        keymap.of([
          // Closed-brackets aware backspace
          ...closeBracketsKeymap,
          // A large set of basic bindings
          ...defaultKeymap,
          // Search-related keys
          ...searchKeymap,
          // Redo/undo keys
          ...historyKeymap,
          // Code folding bindings
          ...foldKeymap,
          // Autocompletion keys
          ...completionKeymap,
          // Keys related to the linter system
          ...lintKeymap,
          indentWithTab
      ]),
      java()
      ]
    })
  }
}
