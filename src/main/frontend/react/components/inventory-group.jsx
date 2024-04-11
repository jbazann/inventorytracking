import React from 'react'
import InventoryPart from "./inventory-part";

export default class InventoryGroup extends React.Component {
    constructor(props){
        super(props);
        this.state = {
            group : props.object,
            displayParts : false,
            hasImg : props.object.id.charCodeAt(0) % 2,
        };
    }

    clickHandler = () => {
        this.setState(prev => ({
            group : prev.group,
            displayParts : !prev.displayParts,
            hasImg : prev.group.id.charCodeAt(0) % 2,
        }));
    }

    render() {
        return <>
            <div className={
                    "flex flex-col border-2 border-slate-600 rounded-2xl aspect-video " +
                    "inv-item-width inv-item-margins overflow-hidden select-none " + 
                    (this.state.displayParts ? "animate-spin " : " ")
                }
                onClick={this.clickHandler}>
                <h2 className="text-center p-1 border-b">
                    {this.state.group.name}
                </h2>
                <div className="flex flex-row h-full min-h-0">
                {this.state.hasImg 
                    ?   <img src="https://api.time.com/wp-content/uploads/2019/08/caveman-spongebob-spongegar.png?w=828" 
                            alt="sample image"
                            className="w-auto h-auto aspect-square">
                        </img>
                    :   null
                }
                </div>
                <h3 className="text-center p-1 border-t">
                    {this.state.group.state}
                </h3>
            </div>
            <ol className="contents">
            {this.state.group.parts.map(part =>
                <li className={this.state.displayParts ? "contents" : "hidden"}
                    key={part.id}>
                    <InventoryPart object={part}></InventoryPart>
                </li>)
            }
            </ol>
        </>
    }
    

}