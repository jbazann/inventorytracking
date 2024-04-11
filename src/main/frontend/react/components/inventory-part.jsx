import React from 'react'

export default class InventoryPart extends React.Component {
    constructor(props) {    
        super(props)
        this.state = {
            part : props.object,
            hasImg : props.object.id.charCodeAt(0) % 2,
        }
    }

    render() {
        return <>
            <div className="flex flex-col border-2 border-slate-600 rounded-2xl aspect-video 
                inv-item-width inv-item-margins overflow-hidden select-none ">
                <h2 className="text-center p-1 border-b">
                    {this.state.part.name}
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
                    {this.state.part.state}
                </h3>
            </div>
        </>
    }
}

