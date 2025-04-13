#version 150

uniform sampler2D DiffuseSampler;
uniform float time;

in vec2 texCoord;
out vec4 fragColor;

vec2 drop(vec2 uv, vec2 pos, float r)
{
    pos.y = fract(pos.y);
    return (uv - pos) * exp(-pow(7.0 * length(uv - pos), 2.0)); // Reduced intensity
}

void main() {
    vec4 original = texture(DiffuseSampler, texCoord);

    vec3 tintColor = vec3(0.9490196078431373, 0.6, 0.1294117647058824);
    vec4 tintedColor = mix(original, vec4(tintColor, 0.22), 0.5);

    vec2 uv2 = texCoord;
    vec2 d = vec2(0.0, 0.0);
    const int n = 100;

    // Slow down the wave's creation by modifying time scaling factor
    for (int i = 0; i < n; i++) {
        vec4 r = texture(DiffuseSampler, vec2(float(i) / float(n), 0.5));
        vec2 pos = r.xy;
        pos.x *= 2.0;

        // Slower wave movement
        pos.y += 10.0 * time * 0.01 * r.a; // Reduced movement speed
        d += 0.05 * drop(uv2.xy, pos, 0.03); // Reduced drop effect intensity
    }

    // Instead of modifying texCoord directly, use a consistent offset for smoothness
    vec2 offset = d * 0.1; // Scale down the effect for smoothness
    fragColor = texture(DiffuseSampler, texCoord + offset);

    fragColor.rgb = mix(fragColor.rgb, tintedColor.rgb, 0.22);
}


// ! Potential Shader #1
/*#version 150

uniform sampler2D DiffuseSampler;
uniform float time;

in vec2 texCoord;
out vec4 fragColor;

vec2 drop(vec2 uv, vec2 pos, float r)
{
    pos.y = fract(pos.y);
    return (uv - pos) * exp(-pow(20.0 * length(uv - pos), 2.0));
}

float random(vec2 seed) {
    return fract(sin(dot(seed, vec2(12.9898, 78.233))) * 43758.5453);
}

void main() {
    vec4 original = texture(DiffuseSampler, texCoord);
    vec3 tintColor = vec3(0.9490196078431373, 0.6, 0.1294117647058824);
    vec4 tintedColor = mix(original, vec4(tintColor, 0.22), 0.5);

    vec2 uv2 = texCoord;
    vec2 d = vec2(0.0, 0.0);
    const int n = 100;

    for (int i = 0; i < n; i++) {
        vec2 randomSeed = vec2(float(i) / float(n), time);
        vec2 pos = vec2(random(randomSeed), random(randomSeed + vec2(1.0, 0.0)));

        pos.x *= 2.0;
        pos.y += 10.0 * time * 0.02;

        d += 0.1 * drop(uv2.xy, pos, 0.03);
    }

    fragColor = texture(DiffuseSampler, texCoord + d);
    fragColor.rgb = mix(fragColor.rgb, tintedColor.rgb, 0.22);
}*/
