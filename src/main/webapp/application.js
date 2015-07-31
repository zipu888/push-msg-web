(function($)
{
    var cometd = $.cometd;

    $(document).ready(function()
    {
        function _connectionEstablished()
        {
            $('#body').append('<div>CometD Connection Established</div>');
        }

        function _connectionBroken()
        {
            $('#body').append('<div>CometD Connection Broken</div>');
        }

        function _connectionClosed()
        {
            $('#body').append('<div>CometD Connection Closed</div>');
        }

        // Function that manages the connection status with the Bayeux server
        var _connected = false;
        function _metaConnect(message)
        {
            if (cometd.isDisconnected())
            {
                _connected = false;
                _connectionClosed();
                return;
            }

            var wasConnected = _connected;
            _connected = message.successful === true;
            if (!wasConnected && _connected)
            {
                _connectionEstablished();
            }
            else if (wasConnected && !_connected)
            {
                _connectionBroken();
            }
        }

        // Function invoked when first contacting the server and
        // when the server has lost the state of this client
        function _metaHandshake(handshake)
        {
            if (handshake.successful)
            {
                cometd.batch(function()
                {
                    cometd.subscribe('/person/notify', function(message)
                    {
                        $('#body').append('<div>个人消息 Says: ' + message.data + '</div>');
                    });
                    cometd.subscribe('/class/232711701680619520', function(message)
                    {
                         $('#body').append('<div>班级消息 Says: ' + message.data + '</div>');
                    });
                    cometd.subscribe('/school/232711701680619520', function(message)
                    {
                         $('#body').append('<div>学校消息 Says: ' + message.data + '</div>');
                    });
                    // Publish on a service channel since the message is for the server only
                });
            }
        }

       /* // Disconnect when the page unloads
        $(window).unload(function()
        {
            cometd.disconnect(true);
        });*/

        var cometURL = location.protocol + "//" + location.host + config.contextPath + "/cometd";
        cometd.configure({
            url: cometURL,
            logLevel: 'debug'
        });
        cometd.getExtension('reload').configure({
            cookieMaxAge: 6000
        });
        // Handshake with additional information.
        var additional = {
            'ssoToken': {
            	//这个需要传给服务器端
                token: 'j7VXaXFJI4RS6eNevPEdokKXPZKknpX2sUytaZRfnRB7NN7qhRl-e0UJW6F3FjkvNStXQaGz0yReaFqB8phI1JT2Ufg2jPMr6AtiVr78O5rDBlr6xOhECqaoTwyVMTiXNMWOMfMsmodC-xrvGnoaaaIRAC6Sv98Bb7GNC5XSOfiG-qJPxl5pMYXgHGJDYalE'
            }
        };
        cometd.websocketEnabled = false;
        // Upon the unload event, call cometd.reload().
        window.onunload=function(){
        	cometd.reload();
        };
        cometd.handshake(additional, _metaHandshake);
        cometd.addListener('/meta/connect', _metaConnect);
    });
})(jQuery);
